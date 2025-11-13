package iuh.fit.xstore.repository;

/*
 * Copyright (c) 2025 by tai
 * All rights reserved.
 *
 * Created: 10/22/2025
 */

import iuh.fit.xstore.model.Product;
import iuh.fit.xstore.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByType(ProductType type);
    List<Product> findByTypeId(int typeId);
    List<Product> findByBrand(String brand);
    Optional<Product> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByName(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.name) LIKE %:query% OR " +
            "LOWER(p.brand) LIKE %:query% OR " +
            "LOWER(p.description) LIKE %:query%")
    List<Product> searchProducts(@Param("query") String query);
}