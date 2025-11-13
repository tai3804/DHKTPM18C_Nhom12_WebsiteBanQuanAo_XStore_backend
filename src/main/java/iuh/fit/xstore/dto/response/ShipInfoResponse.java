package iuh.fit.xstore.dto.response;

import lombok.*;

/**
 * ShipInfoResponse - DTO để trả về thông tin giao hàng
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShipInfoResponse {
    private int id;
    private String recipientName;
    private String recipientPhone;
    private String streetNumber;
    private String streetName;
    private String ward;
    private String district;
    private String city;
    private boolean isDefault;

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
