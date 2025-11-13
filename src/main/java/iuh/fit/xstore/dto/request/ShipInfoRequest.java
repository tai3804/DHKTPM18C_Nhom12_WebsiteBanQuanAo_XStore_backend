package iuh.fit.xstore.dto.request;

import lombok.*;

/**
 * ShipInfoRequest - DTO để tạo hoặc cập nhật thông tin giao hàng
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShipInfoRequest {
    private String recipientName;
    private String recipientPhone;
    private String streetNumber;
    private String streetName;
    private String ward;
    private String district;
    private String city;
    private boolean isDefault;
}
