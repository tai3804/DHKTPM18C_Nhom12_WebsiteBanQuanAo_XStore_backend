package iuh.fit.xstore.repository;

import iuh.fit.xstore.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CommentRepository - Repository cho Comment entity
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * Lấy tất cả comments của một sản phẩm, sắp xếp theo thời gian mới nhất
     */
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.attachments WHERE c.product.id = :productId ORDER BY c.commentAt DESC")
    List<Comment> findByProductIdOrderByCommentAtDesc(@Param("productId") int productId);

    /**
     * Lấy tất cả comments của một author, sắp xếp theo thời gian mới nhất
     */
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.attachments WHERE c.author.id = :authorId ORDER BY c.commentAt DESC")
    List<Comment> findByAuthorIdOrderByCommentAtDesc(@Param("authorId") int authorId);

    /**
     * Đếm số lượng comments của một sản phẩm
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.product.id = :productId")
    long countByProductId(@Param("productId") int productId);

    /**
     * Tính điểm rating trung bình của một sản phẩm
     */
    @Query("SELECT AVG(c.rate) FROM Comment c WHERE c.product.id = :productId")
    Double getAverageRatingByProductId(@Param("productId") int productId);
}