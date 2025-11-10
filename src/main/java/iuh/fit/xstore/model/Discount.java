package iuh.fit.xstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder

@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String title;
    private double value;
    private String description;
    private String type;
    @Enumerated(EnumType.STRING)
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-discount")
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference(value = "order-discount")
    private Order order;

}
