package iuh.fit.xstore.repository;

import iuh.fit.xstore.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem,Integer> {

    List<StockItem> findByStock_Id(int stockId);

    Optional<StockItem> findByStock_IdAndProductInfo_Id(int stockId, int productInfoId);
    
    List<StockItem> findByProductInfo_Id(int productInfoId);

    @Query("SELECT si FROM StockItem si LEFT JOIN FETCH si.productInfo pi LEFT JOIN FETCH pi.product WHERE si.stock.id = :stockId")
    List<StockItem> findByStockIdWithProductInfo(@Param("stockId") int stockId);
}

