package iuh.fit.xstore.service;

import iuh.fit.xstore.model.Comment;
import iuh.fit.xstore.model.CommentAttachment;
import iuh.fit.xstore.model.Product;
import iuh.fit.xstore.model.User;
import iuh.fit.xstore.repository.CommentAttachmentRepository;
import iuh.fit.xstore.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CommentService - Service xử lý logic cho Comment
 */
@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentAttachmentRepository commentAttachmentRepository;

    /**
     * Lấy tất cả comments
     */
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    /**
     * Lấy comment theo ID
     */
    public Comment findById(int id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
    }

    /**
     * Lấy tất cả comments của một sản phẩm
     */
    public List<Comment> findByProductId(int productId) {
        return commentRepository.findByProductIdOrderByCommentAtDesc(productId);
    }

    /**
     * Lấy tất cả comments của một author bằng authorId (int)
     */
    public List<Comment> findByAuthorId(int authorId) {
        return commentRepository.findByAuthorIdOrderByCommentAtDesc(authorId);
    }

    /**
     * Tạo comment mới với attachments
     */
    public Comment create(int productId, int authorId, String authorName, String text, int rate, List<String> imageUrls) {
        // Validate rate (1-5)
        if (rate < 1 || rate > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        // Validate text
        if (text == null || text.trim().isEmpty()) {
            throw new RuntimeException("Comment text cannot be empty");
        }

        // Validate authorName
        if (authorName == null || authorName.trim().isEmpty()) {
            throw new RuntimeException("Author name cannot be empty");
        }

        // Tạo mock Product và User objects (thực tế sẽ lấy từ database)
        Product product = Product.builder().id(productId).build();
        User author = User.builder().id(authorId).build();

        Comment comment = Comment.builder()
            .product(product)
            .author(author)
            .authorName(authorName.trim())
            .text(text.trim())
            .rate(rate)
            .commentAt(LocalDateTime.now())
            .build();

        comment = commentRepository.save(comment);

        // Tạo attachments nếu có
        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<CommentAttachment> attachments = new ArrayList<>();
            for (String imageUrl : imageUrls) {
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    CommentAttachment attachment = CommentAttachment.builder()
                        .comment(comment)
                        .imageUrl(imageUrl.trim())
                        .fileType(imageUrl.toLowerCase().contains(".mp4") ||
                                 imageUrl.toLowerCase().contains(".avi") ||
                                 imageUrl.toLowerCase().contains(".mov") ? "video" : "image")
                        .createdAt(LocalDateTime.now())
                        .build();
                    attachments.add(attachment);
                }
            }
            if (!attachments.isEmpty()) {
                commentAttachmentRepository.saveAll(attachments);
                comment.setAttachments(attachments);
            }
        }

        return comment;
    }

    /**
     * Cập nhật comment
     */
    public Comment update(int id, String text, int rate) {
        Comment existingComment = findById(id);

        // Validate rate
        if (rate < 1 || rate > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        // Validate text
        if (text == null || text.trim().isEmpty()) {
            throw new RuntimeException("Comment text cannot be empty");
        }

        existingComment.setText(text.trim());
        existingComment.setRate(rate);

        return commentRepository.save(existingComment);
    }

    /**
     * Xóa comment và tất cả attachments
     */
    public void delete(int id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found with id: " + id);
        }

        // Xóa tất cả attachments trước
        commentAttachmentRepository.deleteByCommentId(id);

        // Xóa comment
        commentRepository.deleteById(id);
    }

    /**
     * Đếm số lượng comments của một sản phẩm
     */
    public long countByProductId(int productId) {
        return commentRepository.countByProductId(productId);
    }

    /**
     * Tính điểm rating trung bình của một sản phẩm
     */
    public Double getAverageRatingByProductId(int productId) {
        Double avg = commentRepository.getAverageRatingByProductId(productId);
        return avg != null ? avg : 0.0;
    }
}