package iuh.fit.xstore.repository;

import iuh.fit.xstore.model.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor, Integer> {
    List<ProductColor> findByProduct_Id(int productId);
    void deleteByProduct_Id(int productId);
    
    // ✅ JpaRepository đã có flush() method từ PagingAndSortingRepository
    // Không cần khai báo lại
}
