package iuh.fit.xstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockProductResponse {
    private int id;
    private String name;
    private String image;
    private String brand;
    private int totalQuantity;
    private List<StockProductInfoResponse> variants;
}