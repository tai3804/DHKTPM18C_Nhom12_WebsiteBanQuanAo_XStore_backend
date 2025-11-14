package iuh.fit.xstore.repository;

import iuh.fit.xstore.model.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * ProductInfoRepository - Repository cho ProductInfo
 */
public interface ProductInfoRepository extends JpaRepository<ProductInfo, Integer> {
    
    // Lấy tất cả product info theo product ID
    List<ProductInfo> findByProductId(int productId);
    
    // Tìm product info theo product ID, color và size cụ thể
    @Query("SELECT pi FROM ProductInfo pi WHERE pi.product.id = :productId " +
           "AND pi.colorName = :colorName AND pi.sizeName = :sizeName")
    Optional<ProductInfo> findByProductIdAndColorAndSize(
        @Param("productId") int productId,
        @Param("colorName") String colorName,
        @Param("sizeName") String sizeName
    );
    
    // Lấy danh sách màu sắc unique của một sản phẩm
    @Query("SELECT DISTINCT pi.colorName, pi.colorHexCode FROM ProductInfo pi WHERE pi.product.id = :productId")
    List<Object[]> findDistinctColorsByProductId(@Param("productId") int productId);
    
    // Lấy danh sách size unique của một sản phẩm
    @Query("SELECT DISTINCT pi.sizeName FROM ProductInfo pi WHERE pi.product.id = :productId")
    List<String> findDistinctSizesByProductId(@Param("productId") int productId);
    
    // Xóa tất cả product info của một sản phẩm
    void deleteByProductId(int productId);
}
