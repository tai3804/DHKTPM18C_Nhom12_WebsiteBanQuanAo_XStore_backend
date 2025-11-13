package iuh.fit.xstore.dto.request;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO cho ProductSize
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSizeDTO {
    private String name;
    private String description;
}
