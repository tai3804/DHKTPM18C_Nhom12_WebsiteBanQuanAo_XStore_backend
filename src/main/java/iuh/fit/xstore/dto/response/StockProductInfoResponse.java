package iuh.fit.xstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockProductInfoResponse {
    private int id;
    private String colorName;
    private String colorHexCode;
    private String sizeName;
    private String image;
    private int quantity;
}