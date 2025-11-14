package iuh.fit.xstore.service;

import iuh.fit.xstore.dto.response.AppException; // <-- Import
import iuh.fit.xstore.dto.response.ErrorCode; // <-- Import
import iuh.fit.xstore.model.*;
import iuh.fit.xstore.repository.*;
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
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockItemRepository stockItemRepository;

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
    public CartItem addToCart(Integer cartId, Integer productId, Integer stockId, Integer quantity, String color, String size) {
        if (quantity <= 0) {
            throw new AppException(ErrorCode.INVALID_QUANTITY);
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new AppException(ErrorCode.STOCK_NOT_FOUND));

        // 1. Tìm số lượng tồn kho thực tế của sản phẩm này tại kho này
        StockItem stockItem = stockItemRepository.findByStock_IdAndProduct_Id(stockId, productId)
                .orElseThrow(() -> new AppException(ErrorCode.STOCK_ITEM_NOT_FOUND)); // Ném lỗi nếu sản phẩm không có trong kho này

        int availableQuantity = stockItem.getQuantity();

        if (availableQuantity <= 0) {
            throw new AppException(ErrorCode.NOT_ENOUGH_QUANTITY); // Hết hàng
        }

        // 2. Tìm xem item này đã có trong giỏ hàng chưa
        Optional<CartItem> existingItemOpt = cartItemRepository
                .findByCartIdAndProductIdAndStockIdAndColorAndSize(cartId, productId, stockId, color, size);

        CartItem cartItem;
        int newQuantity; // Biến để tính số lượng mới

        if (existingItemOpt.isPresent()) {
            // 3. Đã tồn tại: Tính số lượng mới
            cartItem = existingItemOpt.get();
            newQuantity = cartItem.getQuantity() + quantity;
        } else {
            // 4. Chưa tồn tại: Tạo mới
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setStock(stock); // Lưu stock
            cartItem.setColor(color);
            cartItem.setSize(size);
            newQuantity = quantity;
        }

        // --- KIỂM TRA SỐ LƯỢNG MỚI VỚI TỒN KHO ---
        if (newQuantity > availableQuantity) {
            // Nếu số lượng trong giỏ VƯỢT QUÁ số lượng có sẵn trong kho
            throw new AppException(ErrorCode.NOT_ENOUGH_QUANTITY);
        }
        // --- KẾT THÚC KIỂM TRA ---

        // 5. Set số lượng mới
        cartItem.setQuantity(newQuantity);

        // Thêm vào list của Cart nếu là item mới (chỉ khi chưa tồn tại)
        if (!existingItemOpt.isPresent()) {
            cart.getCartItems().add(cartItem);
        }

        // 6. Tính subtotal và lưu
        Double subTotal = product.getPrice() * cartItem.getQuantity();
        cartItem.setSubTotal(subTotal);
        cartItem = cartItemRepository.save(cartItem);

        // 7. Cập nhật tổng tiền Cart
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