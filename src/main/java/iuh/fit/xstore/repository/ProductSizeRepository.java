package iuh.fit.xstore.repository;

import iuh.fit.xstore.model.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Integer> {
    List<ProductSize> findByProduct_Id(int productId);
    void deleteByProduct_Id(int productId);
    
    // ✅ JpaRepository đã có flush() method từ PagingAndSortingRepository
    // Không cần khai báo lại
}
