package iuh.fit.xstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"product"})
@EqualsAndHashCode(exclude = {"product"})
@Builder
@Entity
@Table(name = "product_sales")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProductSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", unique = true)
    @JsonIgnoreProperties({"productSales"})
    private Product product;

    @Column(name = "original_price")
    private Double originalPrice;

    @Column(name = "discount_percent")
    private int discountPercent;

    @Column(name = "discounted_price")
    private Double discountedPrice;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;
}