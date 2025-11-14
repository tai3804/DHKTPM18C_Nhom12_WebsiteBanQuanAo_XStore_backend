package iuh.fit.xstore.services;

import iuh.fit.xstore.model.Comment;
import iuh.fit.xstore.model.Product;
import iuh.fit.xstore.model.User;
import iuh.fit.xstore.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CommentService - Service xử lý logic cho Comment
 */
@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

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
     * Lấy tất cả comments của một author (author lưu dưới dạng String)
     */
    public List<Comment> findByAuthor(String author) {
        return commentRepository.findByAuthorOrderByCommentAtDesc(author);
    }

    /**
     * Convenience: Lấy tất cả comments của một author bằng authorId (int)
     */
    public List<Comment> findByAuthorId(int authorId) {
        return commentRepository.findByAuthorOrderByCommentAtDesc(String.valueOf(authorId));
    }

    /**
     * Tạo comment mới
     */
    public Comment create(int productId, int authorId, String text, String image, int rate) {
        // Validate rate (1-5)
        if (rate < 1 || rate > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        // Validate text
        if (text == null || text.trim().isEmpty()) {
            throw new RuntimeException("Comment text cannot be empty");
        }

        // Tạo mock Product và User objects (thực tế sẽ lấy từ database)
        Product product = Product.builder().id(productId).build();

        Comment comment = Comment.builder()
            .product(product)
            .author(String.valueOf(authorId))
                .text(text.trim())
                .image(image)
                .rate(rate)
                .commentAt(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }

    /**
     * Cập nhật comment
     */
    public Comment update(int id, String text, String image, int rate) {
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
        existingComment.setImage(image);
        existingComment.setRate(rate);

        return commentRepository.save(existingComment);
    }

    /**
     * Xóa comment
     */
    public void delete(int id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found with id: " + id);
        }
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