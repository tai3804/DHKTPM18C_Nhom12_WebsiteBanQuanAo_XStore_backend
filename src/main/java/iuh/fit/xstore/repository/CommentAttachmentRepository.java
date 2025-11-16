package iuh.fit.xstore.repository;

import iuh.fit.xstore.model.CommentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CommentAttachmentRepository - Repository cho CommentAttachment entity
 */
@Repository
public interface CommentAttachmentRepository extends JpaRepository<CommentAttachment, Integer> {

    /**
     * Lấy tất cả attachments của một comment
     */
    @Query("SELECT ca FROM CommentAttachment ca WHERE ca.comment.id = :commentId ORDER BY ca.createdAt ASC")
    List<CommentAttachment> findByCommentIdOrderByCreatedAtAsc(@Param("commentId") int commentId);

    /**
     * Xóa tất cả attachments của một comment
     */
    void deleteByCommentId(int commentId);
}