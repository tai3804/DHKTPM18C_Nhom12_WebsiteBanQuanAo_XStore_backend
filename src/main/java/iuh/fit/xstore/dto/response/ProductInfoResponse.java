package iuh.fit.xstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInfoResponse {
    private int id;
    private String colorName;
    private String colorHexCode;
    private String sizeName;
    private int quantity;
    private String image;
}
