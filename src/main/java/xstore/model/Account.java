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
@Builder

@Entity
@Table(name = "account")
public class Account {
    @Id
    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    private User user;
}
