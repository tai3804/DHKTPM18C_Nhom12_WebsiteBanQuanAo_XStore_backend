package xstore.model;

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
@ToString
@EqualsAndHashCode
@Builder


@Entity
@Table(name = "product")
public class Product {
    @Id
    private String id;

    private String name;
    private String description;
    private String image;
    private String type;
    private String brand;
    private String size;
    private String color;
    private String price;
}
