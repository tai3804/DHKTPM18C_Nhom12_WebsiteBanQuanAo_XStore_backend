package iuh.fit.xstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"stock", "productInfo"}) // tránh đệ quy
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "stock_items")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_info_id")
    @JsonIgnoreProperties({"product", "stockItems"})
    private ProductInfo productInfo;

    private int quantity;
}
