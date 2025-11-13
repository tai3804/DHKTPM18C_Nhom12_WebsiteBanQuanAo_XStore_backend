package iuh.fit.xstore.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder

@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("order-items")
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "order")
    @JsonManagedReference("order-discounts")
    private List<Discount> discounts;

    private double total;
    private double subtotal;           // Tổng tiền hàng trước giảm giá
    private double discountAmount;     // Số tiền giảm giá
    private double shippingFee;        // Phí vận chuyển
    private String paymentMethod;      // CASH, CARD, MOMO, ZALOPAY
    private String shippingAddress;    // Địa chỉ giao hàng
    private String phoneNumber;        // Số điện thoại nhận hàng
    private String notes;              // Ghi chú đơn hàng
}
