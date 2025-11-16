package iuh.fit.xstore.repository;

import iuh.fit.xstore.model.ProductSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductSalesRepository extends JpaRepository<ProductSales, Integer> {
    Optional<ProductSales> findByProductId(int productId);
}