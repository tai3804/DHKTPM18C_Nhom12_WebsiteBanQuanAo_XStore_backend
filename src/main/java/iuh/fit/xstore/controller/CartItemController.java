package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.AddToCartRequest;
import iuh.fit.xstore.dto.request.UpdateCartItemRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.model.CartItem;
import iuh.fit.xstore.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    // ✅ Lấy tất cả cart items
    @GetMapping
    public ResponseEntity<?> getAllCartItems() {
        try {
            List<CartItem> cartItems = cartItemService.getAllCartItems();

            ApiResponse<List<CartItem>> response = new ApiResponse<>(
                    200,
                    "Lấy danh sách cart items thành công",
                    cartItems
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

    // ✅ Lấy cart items theo cart ID
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<?> getCartItemsByCartId(@PathVariable Integer cartId) {
        try {
            List<CartItem> cartItems = cartItemService.getCartItemsByCartId(cartId);

            ApiResponse<List<CartItem>> response = new ApiResponse<>(
                    200,
                    "Lấy danh sách cart items thành công",
                    cartItems
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    "Không tìm thấy cart items",
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    // ✅ Lấy cart item theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCartItemById(@PathVariable Integer id) {
        try {
            CartItem cartItem = cartItemService.getCartItemById(id);

            ApiResponse<CartItem> response = new ApiResponse<>(
                    200,
                    "Lấy cart item thành công",
                    cartItem
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    "Không tìm thấy cart item",
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request) {
        try {
            CartItem cartItem = cartItemService.addToCart(
                    request.getCartId(),
                    request.getProductId(),
                    request.getStockId(),
                    request.getQuantity(),
                    request.getProductInfoId()
            );

            ApiResponse<CartItem> response = new ApiResponse<>(
                    200,
                    "Thêm sản phẩm vào giỏ hàng thành công",
                    cartItem
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Integer id,
            @RequestBody UpdateCartItemRequest request
    ) {
        try {
            CartItem cartItem = cartItemService.updateQuantity(id, request.getQuantity());

            ApiResponse<CartItem> response = new ApiResponse<>(
                    200,
                    "Cập nhật số lượng thành công",
                    cartItem
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable Integer id) {
        try {
            cartItemService.removeFromCart(id);

            ApiResponse<String> response = new ApiResponse<>(
                    200,
                    "Xóa sản phẩm khỏi giỏ hàng thành công",
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