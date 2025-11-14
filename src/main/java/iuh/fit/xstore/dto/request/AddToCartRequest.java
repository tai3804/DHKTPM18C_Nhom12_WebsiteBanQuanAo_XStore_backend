package iuh.fit.xstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    private Integer cartId;
    private Integer productId;
    private Integer stockId;
    private Integer quantity = 1;
    private String color;
    private String size;
}