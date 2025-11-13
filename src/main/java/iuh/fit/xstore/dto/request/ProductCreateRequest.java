package iuh.fit.xstore.dto.request;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO để nhận dữ liệu Product từ frontend khi tạo/cập nhật
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCreateRequest {
    private int id;
    private String name;
    private String description;
    private String image;
    private int typeId;  // Chỉ nhận ID của type, không nhận cả object
    private String brand;
    private String fabric;
    private double priceInStock;
    private double price;
    private List<ProductColorDTO> colors = new ArrayList<>();
    private List<ProductSizeDTO> sizes = new ArrayList<>();
}
