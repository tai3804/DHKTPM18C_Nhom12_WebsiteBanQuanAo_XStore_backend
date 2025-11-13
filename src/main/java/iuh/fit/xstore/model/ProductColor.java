package iuh.fit.xstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "product") // tránh vòng lặp toString()
@EqualsAndHashCode(exclude = "product")
@Builder

@Entity
@Table(name = "product_colors")
public class ProductColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // ✅ Tránh vòng lặp JSON: dùng JsonBackReference
    // ⚠️ JsonBackReference sẽ KHÔNG deserialize product từ JSON input
    // Nhưng vẫn giữ để tránh vòng lặp khi trả về JSON response
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    private String name;    // VD: "Đỏ", "Xanh"
    private String hexCode; // VD: "#FF0000"
}

