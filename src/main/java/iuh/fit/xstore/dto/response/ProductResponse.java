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
public class ProductResponse {
    private int id;
    private String name;
    private String description;
    private String image;
    private String typeName;
    private String brand;
    private String fabric;
    private double priceInStock;
    private double price;

    private List<ProductInfoResponse> productInfos;
    private List<CommentResponse> comments;
}
