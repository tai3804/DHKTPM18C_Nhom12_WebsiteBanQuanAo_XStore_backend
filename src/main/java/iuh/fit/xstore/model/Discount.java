package iuh.fit.xstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(exclude = {"user", "order"})

@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;
    private String title;
    private String description;
    
    // Discount amount (fixed value) - vd: 50000 đ
    private double discountAmount;
    
    // Discount percent (percentage) - vd: 10 (%)
    private double discountPercent;
    
    // Loại discount: FIXED, PERCENT
    private String type;
    
    // Category: PRODUCT (giảm tiền), SHIPPING (giảm ship)
    private String category;
    
    // Usage tracking
    private int usageCount;
    private int maxUsage;
    
    // Validity period
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "is_active")
    @JsonProperty("isActive")
    private Boolean isActive;

    // Loại user được phép sử dụng discount này
    @Column(name = "valid_user_type")
    @Enumerated(EnumType.STRING)
    private UserType validUserType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference("order-discounts")
    private Order order;
    
    /**
     * Kiểm tra discount có hợp lệ không
     * - Phải active
     * - Chưa hết hạn
     * - Chưa vượt maxUsage
     */
    public boolean isValid() {
        if (!isActive) return false;
        
        LocalDate now = LocalDate.now();
        if (startDate != null && now.isBefore(startDate)) return false;
        if (endDate != null && now.isAfter(endDate)) return false;
        
        if (maxUsage > 0 && usageCount >= maxUsage) return false;
        
        return true;
    }

    /**
     * Kiểm tra discount có hợp lệ không với user cụ thể
     * - Phải active
     * - Chưa hết hạn
     * - Chưa vượt maxUsage
     * - UserType của user phải khớp với validUserType (nếu có)
     */
    public boolean isValidForUser(User user) {
        if (!isValid()) return false;
        
        // Nếu validUserType được set, kiểm tra user type
        if (validUserType != null) {
            if (user == null || user.getUserType() != validUserType) {
                return false;
            }
        }
        
        return true;
    }
}
