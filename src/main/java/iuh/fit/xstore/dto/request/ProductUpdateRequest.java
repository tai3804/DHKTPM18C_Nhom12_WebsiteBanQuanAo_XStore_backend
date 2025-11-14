package iuh.fit.xstore.dto.request;

import iuh.fit.xstore.model.ProductInfo;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class ProductUpdateRequest {
    private String name;
    private String description;
    private String image;
    private String brand;
    private String fabric;
    private Double price;
    private Double priceInStock;
    private int typeId;  // ProductType ID

    // Thay color/size bằng ProductInfo
    private List<ProductInfo> productInfos;
}
