package xstore.model;

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
@Table(name = "cart_item")
public class CartItem {
    @EmbeddedId
    private  CartID id;

    @MapsId("cartID")
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @MapsId("productID")
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private double subTotal;
}

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Builder

@Embeddable
class CartID {
    private String cartID;
    private String productID;
}

