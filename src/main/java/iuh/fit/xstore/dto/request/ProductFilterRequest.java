package iuh.fit.xstore.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterRequest {
    private Integer productTypeId;
    private String productTypeName;
    private Double minPrice;
    private Double maxPrice;
    private String sortBy; // price-asc, price-desc, name-asc, name-desc, newest
    private Integer page;
    private Integer size;
}
