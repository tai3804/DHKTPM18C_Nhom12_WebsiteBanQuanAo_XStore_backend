package iuh.fit.xstore.repository;

import iuh.fit.xstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> { // ✅ Long -> Integer

    // ✅ Đổi Long thành Integer
    Optional<CartItem> findByCartIdAndProductId(Integer cartId, Integer productId);

    List<CartItem> findByCartId(Integer cartId);
}