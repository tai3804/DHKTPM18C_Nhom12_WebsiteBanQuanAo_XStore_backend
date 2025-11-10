package iuh.fit.xstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    private Integer cartId;    // ✅ Đổi từ Long sang Integer
    private Integer productId; // ✅ Đổi từ Long sang Integer
    private Integer quantity = 1;
}