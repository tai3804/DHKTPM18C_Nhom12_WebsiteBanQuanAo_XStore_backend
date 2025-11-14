package iuh.fit.xstore.repository;

/*
 * Copyright (c) 2025 by tai
 * All rights reserved.
 *
 * Created: 10/15/2025
 */


import iuh.fit.xstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
   Optional<User> findByAccountUsername(String accountUsername);
    boolean existsByAccountUsername(String accountUsername);
    User getByAccountUsername(String accountUsername);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.firstName) LIKE %:query% OR " +
            "LOWER(u.lastName) LIKE %:query% OR " +
            "LOWER(u.email) LIKE %:query% OR " +
            "LOWER(u.account.username) LIKE %:query%")
    List<User> searchUsers(@Param("query") String query);

    Optional<User> findByEmail(String email);

    Optional<User>  findByPhone(String phone);

    boolean existsByEmail(String email);
}
