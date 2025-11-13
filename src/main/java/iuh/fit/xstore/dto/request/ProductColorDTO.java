package iuh.fit.xstore.dto.request;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO cho ProductColor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductColorDTO {
    private String name;
    private String hexCode;
}
