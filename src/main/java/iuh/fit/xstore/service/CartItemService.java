package iuh.fit.xstore.service;

import iuh.fit.xstore.dto.response.AppException; // <-- Import
import iuh.fit.xstore.dto.response.ErrorCode; // <-- Import
import iuh.fit.xstore.model.Cart;
import iuh.fit.xstore.model.CartItem;
import iuh.fit.xstore.model.Product;
import iuh.fit.xstore.repository.CartItemRepository;
import iuh.fit.xstore.repository.CartRepository;
import iuh.fit.xstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public List<CartItem> getCartItemsByCartId(Integer cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    public CartItem getCartItemById(Integer cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND)); // <-- Sửa lỗi
    }

    @Transactional
    public CartItem addToCart(Integer cartId, Integer productId, Integer quantity) {
        if (quantity <= 0) {
            throw new AppException(ErrorCode.INVALID_QUANTITY); // <-- Sửa lỗi
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND)); // (Giả sử lỗi này)

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)); // <-- Sửa lỗi

        // SỬA LOGIC: Kiểm tra item đã tồn tại chưa
        Optional<CartItem> existingItem = cartItemRepository
                .findByCartIdAndProductId(cartId, productId);

        CartItem cartItem;
        if (existingItem.isPresent()) {
            // Đã tồn tại: Cập nhật số lượng
            cartItem = existingItem.get();
            Integer newQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(newQuantity);
        } else {
            // Chưa tồn tại: Tạo mới
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.getCartItems().add(cartItem); // Thêm vào list của Cart
        }

        // Tính subtotal và lưu
        Double subTotal = product.getPrice() * cartItem.getQuantity();
        cartItem.setSubTotal(subTotal);
        cartItem = cartItemRepository.save(cartItem);

        // Cập nhật tổng tiền Cart
        updateCartTotal(cart);

        return cartItem;
    }

    @Transactional
    public CartItem updateQuantity(Integer cartItemId, Integer quantity) {
        if (quantity <= 0) {
            // Nếu update số lượng <= 0, ta nên XÓA item đó
            removeFromCart(cartItemId);
            return null; // Trả về null vì item đã bị xóa
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND)); // <-- Sửa lỗi

        cartItem.setQuantity(quantity);
        Double subTotal = cartItem.getProduct().getPrice() * quantity;
        cartItem.setSubTotal(subTotal);

        cartItem = cartItemRepository.save(cartItem);

        // Cập nhật tổng tiền Cart
        updateCartTotal(cartItem.getCart());

        return cartItem;
    }

    @Transactional
    public void removeFromCart(Integer cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND)); // <-- Sửa lỗi

        Cart cart = cartItem.getCart();
        cart.getCartItems().remove(cartItem); // Xóa khỏi collection
        cartItemRepository.delete(cartItem);  // Xóa khỏi DB

        // Cập nhật tổng tiền Cart
        updateCartTotal(cart);
    }

    // Hàm helper tính tổng tiền
    private void updateCartTotal(Cart cart) {
        // Lấy lại danh sách items mới nhất từ DB
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        Double total = items.stream()
                .mapToDouble(CartItem::getSubTotal)
                .sum();
        cart.setTotal(total);
        cartRepository.save(cart);
    }
}