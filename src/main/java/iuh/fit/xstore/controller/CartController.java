package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.CreateCartRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.model.Cart;
import iuh.fit.xstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // ✅ Lấy tất cả giỏ hàng
    @GetMapping
    public ResponseEntity<?> getAllCarts() {
        try {
            List<Cart> carts = cartService.getAllCarts();

            ApiResponse<List<Cart>> response = new ApiResponse<>(
                    200,
                    "Lấy danh sách giỏ hàng thành công",
                    carts
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // ✅ Lấy giỏ hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCartById(@PathVariable Integer id) {
        try {
            Cart cart = cartService.getCartById(id);

            ApiResponse<Cart> response = new ApiResponse<>(
                    200,
                    "Lấy giỏ hàng thành công",
                    cart
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    "Không tìm thấy giỏ hàng",
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    // ✅ Tạo giỏ hàng mới cho user
    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody CreateCartRequest request) {
        try {
            Cart cart = cartService.createCartForUser(request.getUserId());

            ApiResponse<Cart> response = new ApiResponse<>(
                    200,
                    "Tạo giỏ hàng thành công",
                    cart
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // ✅ Lấy giỏ hàng của user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable Integer userId) {
        try {
            Cart cart = cartService.getCartByUserId(userId);

            ApiResponse<Cart> response = new ApiResponse<>(
                    200,
                    "Lấy giỏ hàng thành công",
                    cart
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    "Không tìm thấy giỏ hàng",
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    // ✅ Xóa giỏ hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Integer id) {
        try {
            cartService.deleteCart(id);

            ApiResponse<String> response = new ApiResponse<>(
                    200,
                    "Xóa giỏ hàng thành công",
                    null
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}