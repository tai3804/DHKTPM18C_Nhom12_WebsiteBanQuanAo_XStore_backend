package iuh.fit.xstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO nhận từ frontend khi checkout
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutRequest {
    private int userId;                    // ID người dùng
    private int cartId;                    // ID giỏ hàng
    private List<CheckoutItemRequest> items;  // Danh sách sản phẩm muốn mua
    private String paymentMethod;          // Phương thức thanh toán: CASH, CARD, MOMO, ZALOPAY
    private String shippingAddress;        // Địa chỉ giao hàng
    private String phoneNumber;            // Số điện thoại nhận hàng
    private String notes;                  // Ghi chú đơn hàng
    private double discountAmount;         // Số tiền giảm giá
    private int discountId;                // ID mã giảm giá (optional)
}

class CheckoutItemRequest {
    public int cartItemId;
    public int productId;
    public int quantity;
    public double price;
    // getter setter
}
