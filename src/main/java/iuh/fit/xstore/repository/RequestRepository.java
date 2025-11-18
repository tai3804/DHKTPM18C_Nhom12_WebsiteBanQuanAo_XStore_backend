package iuh.fit.xstore.repository;

import iuh.fit.xstore.model.Request;
import iuh.fit.xstore.model.RequestStatus;
import iuh.fit.xstore.model.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    List<Request> findByUserId(int userId);

    List<Request> findByOrderId(int orderId);

    List<Request> findByStatus(RequestStatus status);

    List<Request> findByType(RequestType type);

    @Query("SELECT COUNT(r) FROM Request r WHERE r.type = :type")
    long countByType(@Param("type") RequestType type);

    @Query("SELECT COUNT(r) FROM Request r WHERE r.type = :type AND r.createdAt >= :startDate")
    long countByTypeAndCreatedAtAfter(@Param("type") RequestType type, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(r) FROM Request r WHERE r.status = :status")
    long countByStatus(@Param("status") RequestStatus status);

    @Query("SELECT r FROM Request r WHERE r.type = :type AND r.status = :status")
    List<Request> findByTypeAndStatus(@Param("type") RequestType type, @Param("status") RequestStatus status);
}