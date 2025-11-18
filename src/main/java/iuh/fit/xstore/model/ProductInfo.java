package iuh.fit.xstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "product")
@EqualsAndHashCode(exclude = "product")
@Builder
@Entity
@Table(name = "product_info")
public class ProductInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"productInfos", "comments", "orderItems", "stockItems"})
    private Product product;

    private String colorName;
    private String colorHexCode;
    private String sizeName;
    private String image;

    @OneToMany(mappedBy = "productInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<StockItem> stockItems = new ArrayList<>();
}
