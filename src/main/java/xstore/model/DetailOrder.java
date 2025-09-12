package xstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

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
@Table(name="detail_order")
public class DetailOrder {
    @EmbeddedId
    private DetailOrderID id;

    @MapsId("orderID")
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @MapsId("productID")
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private double price;
    private double subTotal;
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder

@Embeddable
class DetailOrderID implements Serializable {
    private String orderID;
    private String productID;
}
