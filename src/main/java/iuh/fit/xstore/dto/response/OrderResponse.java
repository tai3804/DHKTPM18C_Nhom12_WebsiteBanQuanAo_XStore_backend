package iuh.fit.xstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO trả về thông tin đơn hàng sau khi checkout
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private int orderId;
    private int userId;
    private LocalDate createdAt;
    private String status;                 // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    private List<OrderItemResponse> items;
    private double subtotal;
    private double discountAmount;
    private double shippingFee;
    private double total;
    private String paymentMethod;
    private String shippingAddress;
    private String phoneNumber;
    private String notes;
}

class OrderItemResponse {
    public int itemId;
    public int productId;
    public String productName;
    public String productImage;
    public int quantity;
    public double price;
    public double subtotal;
}
