package iuh.fit.xstore.service;

import iuh.fit.xstore.model.Cart;
import iuh.fit.xstore.model.User;
import iuh.fit.xstore.repository.CartRepository;
import iuh.fit.xstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Lấy tất cả giỏ hàng
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    // ✅ Lấy giỏ hàng theo ID
    public Cart getCartById(Integer cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart không tồn tại với ID: " + cartId));
    }

    // ✅ Tạo giỏ hàng mới và gán cho user
    @Transactional
    public Cart createCartForUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if (user.getCart() != null) {
            return user.getCart();
        }

        Cart cart = new Cart();
        cart.setTotal(0.0);
        cart = cartRepository.save(cart);

        user.setCart(cart);
        userRepository.save(user);

        return cart;
    }

    // ✅ Lấy giỏ hàng của user
    public Cart getCartByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if (user.getCart() == null) {
            throw new RuntimeException("User chưa có giỏ hàng");
        }

        return user.getCart();
    }

    // ✅ Xóa giỏ hàng
    @Transactional
    public void deleteCart(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart không tồn tại"));

        cartRepository.delete(cart);
    }
}