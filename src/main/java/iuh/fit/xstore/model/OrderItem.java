package iuh.fit.xstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

/**
 * Copyright (c) 2025 by Tai.
 * All rights reserved.
 * This file is part of X-Store.
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder

@Entity
@Table(name="order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference("order-items")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "stock_id") // <-- THÊM DÒNG NÀY
    private Stock stock;

    private int quantity;
    private double unitPrice;      // Giá đơn vị tại thời điểm mua
    private double subTotal;
    private String color;          // Màu sắc được chọn
    private String size;           // Kích cỡ được chọn
}