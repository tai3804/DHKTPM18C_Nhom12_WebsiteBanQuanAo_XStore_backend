package xstore.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder

@Entity
@Table(name = "detail_stock")
public class DetailStock {
    @EmbeddedId
    private DeatilStockID id;

    @MapsId("stockID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @MapsId("productID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodcut_id")
    private Product product;

    private int quantity;
    private  double price_in_stock;
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder

@Embeddable
class DeatilStockID {
    private String stockID;
    private String productID;
}
