package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Address;
import iuh.fit.xstore.service.AddressService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@AllArgsConstructor
@Slf4j
public class AddressController {
    private final AddressService addressService;

    /**
     * GET /api/addresses/user/{userId}: Lấy tất cả địa chỉ của user
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<Address>> getUserAddresses(@PathVariable("userId") int userId) {
        log.info("Getting addresses for user: {}", userId);
        List<Address> addresses = addressService.findByUserId(userId);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(), "Fetch addresses successfully", addresses);
    }

    /**
     * GET /api/addresses: Lấy tất cả địa chỉ
     */
    @GetMapping
    public ApiResponse<List<Address>> getAddresses() {
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(), "Fetch addresses successfully", addressService.findAll());
    }

    /**
     * GET /api/addresses/{id}: Lấy địa chỉ theo ID
     */
    @GetMapping("/{id}")
    public ApiResponse<Address> getAddress(@PathVariable("id") int id) {
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(), "Fetch address successfully", addressService.findById(id));
    }

    /**
     * POST /api/addresses: Tạo địa chỉ mới
     */
    @PostMapping
    public ApiResponse<Address> createAddress(
            @RequestBody Address address,
            @RequestParam("userId") int userId) {
        log.info("📍 Creating new address for user: {}", userId);
        Address createdAddress = addressService.createAddressForUser(address, userId);
        return new ApiResponse<>(SuccessCode.ADDRESS_CREATED.getCode(), "Address created successfully", createdAddress);
    }

    /**
     * PUT /api/addresses/{id}: Cập nhật địa chỉ
     */
    @PutMapping("/{id}")
    public ApiResponse<Address> updateAddress(@PathVariable("id") int id, @RequestBody Address address) {
        log.info("📍 Updating address: {}", id);
        address.setId(id);
        Address updatedAddress = addressService.updateAddress(address);
        return new ApiResponse<>(SuccessCode.ADDRESS_UPDATED.getCode(), "Address updated successfully", updatedAddress);
    }

    /**
     * DELETE /api/addresses/{id}: Xóa địa chỉ
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteAddress(@PathVariable("id") int id) {
        log.info("📍 Deleting address: {}", id);
        addressService.deleteAddress(id);
        return new ApiResponse<>(SuccessCode.ADDRESS_DELETED.getCode(), "Address deleted successfully", "Address " + id + " deleted");
    }

    /**
     * PUT /api/addresses/{id}/set-default: Đặt địa chỉ làm mặc định
     */
    @PutMapping("/{id}/set-default")
    public ApiResponse<Address> setDefaultAddress(@PathVariable("id") int id, @RequestParam("userId") int userId) {
        log.info("📍 Setting default address: {} for user: {}", id, userId);
        Address address = addressService.setDefaultAddress(id, userId);
        return new ApiResponse<>(SuccessCode.ADDRESS_UPDATED.getCode(), "Default address set successfully", address);
    }
}
