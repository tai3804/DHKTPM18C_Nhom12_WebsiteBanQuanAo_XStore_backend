package iuh.fit.xstore.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * ShipInfo entity - Lưu trữ thông tin giao hàng (người nhận, số điện thoại, địa chỉ)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder

@Entity
@Table(name = "ship_infos")
public class ShipInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Liên kết đến User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Tên người nhận
    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    // Số điện thoại người nhận
    @Column(name = "recipient_phone", nullable = false)
    private String recipientPhone;

    // Số nhà/tòa
    @Column(name = "street_number")
    private String streetNumber;

    // Tên đường
    @Column(name = "street_name")
    private String streetName;

    // Phường/Xã
    private String ward;

    // Quận/Huyện
    private String district;

    // Thành phố/Tỉnh
    private String city;

    // Là địa chỉ mặc định không?
    @Column(name = "is_default")
    @Builder.Default
    private boolean isDefault = false;

    /**
     * Tạo full address string từ các thành phần
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (streetNumber != null && !streetNumber.isEmpty()) {
            sb.append(streetNumber).append(" ");
        }
        if (streetName != null && !streetName.isEmpty()) {
            sb.append(streetName).append(", ");
        }
        if (ward != null && !ward.isEmpty()) {
            sb.append(ward).append(", ");
        }
        if (district != null && !district.isEmpty()) {
            sb.append(district).append(", ");
        }
        if (city != null && !city.isEmpty()) {
            sb.append(city);
        }
        return sb.toString().trim();
    }
}
