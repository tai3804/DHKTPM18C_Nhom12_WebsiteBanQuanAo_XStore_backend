package iuh.fit.xstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSalesRequest {
    private int productId;
    private int discountPercent;
    private double discountedPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}