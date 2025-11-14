package iuh.fit.xstore.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductInfoRequest {
    private String colorName;
    private String colorHexCode;
    private String sizeName;
    private int quantity;
    private String image;
}
