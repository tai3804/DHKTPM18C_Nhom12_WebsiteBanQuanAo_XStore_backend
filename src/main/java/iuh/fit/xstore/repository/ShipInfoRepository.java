package iuh.fit.xstore.repository;

import iuh.fit.xstore.model.ShipInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShipInfoRepository extends JpaRepository<ShipInfo, Integer> {
    // Lấy tất cả ship info của một user
    List<ShipInfo> findByUserId(int userId);

    // Lấy ship info mặc định của user
    @Query("SELECT s FROM ShipInfo s WHERE s.user.id = ?1 AND s.isDefault = true")
    Optional<ShipInfo> findDefaultByUserId(int userId);
}
