package iuh.fit.xstore.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2025 by Tai.
 * All rights reserved.
 * This file is part of X-Store.
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"colors", "sizes", "orderItems"})
@EqualsAndHashCode(exclude = {"colors", "sizes", "orderItems"})
@Builder

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;
    private String image;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType type;

    private String brand;
    private String fabric;

    @Column(name = "price_in_stock")
    private double priceInStock;
    private double price;

    // ✅ Một product có nhiều màu
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Điều khiển serialization hướng từ Product -> Color
    private List<ProductColor> colors = new ArrayList<>();

    // ✅ Một product có nhiều size
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Điều khiển serialization hướng từ Product -> Size
    private List<ProductSize> sizes = new ArrayList<>();

    // ✅ OrderItem tham chiếu ngược về Product — không cần JSON
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();
}
