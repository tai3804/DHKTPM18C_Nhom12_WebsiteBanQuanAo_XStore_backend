package iuh.fit.xstore.service;

import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.model.Address;
import iuh.fit.xstore.model.User;
import iuh.fit.xstore.repository.AddressRepository;
import iuh.fit.xstore.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AddressService {
    private final AddressRepository addressRepo;
    private final UserRepository userRepo;

    /**
     * Lấy tất cả địa chỉ
     */
    public List<Address> findAll() {
        return addressRepo.findAll();
    }

    /**
     * Lấy địa chỉ theo ID
     */
    public Address findById(int id) {
        return addressRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
    }

    /**
     * Lấy tất cả địa chỉ của một user
     */
    public List<Address> findByUserId(int userId) {
        log.info("📍 Fetching addresses for user: {}", userId);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        // TODO: Thêm relationship User -> List<Address> trong entity
        // Tạm thời trả về tất cả addresses (sẽ fix khi có relationship)
        return addressRepo.findAll();
    }

    /**
     * Tạo địa chỉ mới cho user
     */
    public Address createAddressForUser(Address address, int userId) {
        log.info("📍 Creating address for user: {}", userId);
        
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // TODO: Thêm relationship User -> List<Address> trong entity
        // Tạm thời chỉ save address, không check default
        // vì chưa có relationship để query addresses của user

        Address savedAddress = addressRepo.save(address);
        log.info("✅ Address created: {}", savedAddress.getId());
        
        return savedAddress;
    }

    /**
     * Tạo địa chỉ mới (chung)
     */
    public Address createAddress(Address address) {
        return addressRepo.save(address);
    }

    /**
     * Cập nhật địa chỉ
     */
    public Address updateAddress(Address address) {
        Address existedAddress = findById(address.getId());

        if (address.getStreetNumber() != null) {
            existedAddress.setStreetNumber(address.getStreetNumber());
        }
        if (address.getStreetName() != null) {
            existedAddress.setStreetName(address.getStreetName());
        }
        if (address.getWard() != null) {
            existedAddress.setWard(address.getWard());
        }
        if (address.getDistrict() != null) {
            existedAddress.setDistrict(address.getDistrict());
        }
        if (address.getCity() != null) {
            existedAddress.setCity(address.getCity());
        }

        return addressRepo.save(existedAddress);
    }

    /**
     * Xóa địa chỉ
     */
    public int deleteAddress(int id) {
        findById(id);
        addressRepo.deleteById(id);
        log.info("✅ Address deleted: {}", id);
        return id;
    }

    /**
     * Đặt địa chỉ làm mặc định cho user
     */
    public Address setDefaultAddress(int addressId, int userId) {
        log.info("📍 Setting default address {} for user {}", addressId, userId);
        
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Address address = findById(addressId);

        // Reset tất cả địa chỉ của user
        // TODO: Implement khi có relationship User -> List<Address>

        // Set địa chỉ này là mặc định
        address.setDefault(true);
        Address saved = addressRepo.save(address);
        
        log.info("✅ Default address set: {}", addressId);
        return saved;
    }
}