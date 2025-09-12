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
@Builder

@Entity
@Table(name = "stock")
public class Stock implements Serializable {
    @Id
    private  String id;

    private String name;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
