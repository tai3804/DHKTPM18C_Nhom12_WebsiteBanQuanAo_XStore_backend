package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.CommentCreateRequest;
import iuh.fit.xstore.dto.request.CommentUpdateRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.model.Comment;
import iuh.fit.xstore.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // GET all
    @GetMapping
    public ResponseEntity<?> getAllComments() {
        List<Comment> comments = commentService.findAll();
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Lấy danh sách bình luận thành công", comments)
        );
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable int id) {
        Comment comment = commentService.findById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Lấy bình luận thành công", comment)
        );
    }

    // GET comments of product
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getCommentsByProductId(@PathVariable int productId) {
        List<Comment> comments = commentService.findByProductId(productId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Lấy bình luận sản phẩm thành công", comments)
        );
    }

    // GET comments by author
    @GetMapping("/author/{authorId}")
    public ResponseEntity<?> getCommentsByAuthorId(@PathVariable int authorId) {
        List<Comment> comments = commentService.findByAuthorId(authorId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Lấy bình luận của người dùng thành công", comments)
        );
    }

    // CREATE comment
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentCreateRequest request) {
        Comment comment = commentService.create(
                request.getProductId(),
                request.getAuthorId(),
                request.getText(),
                request.getImage(),
                request.getRate()
        );

        return ResponseEntity.status(201).body(
                new ApiResponse<>(201, "Tạo bình luận thành công", comment)
        );
    }

    // UPDATE comment
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(
            @PathVariable int id,
            @RequestBody CommentUpdateRequest request) {

        Comment updated = commentService.update(
                id,
                request.getText(),
                request.getImage(),
                request.getRate()
        );

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cập nhật bình luận thành công", updated)
        );
    }

    // DELETE comment
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable int id) {
        commentService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Xóa bình luận thành công", null)
        );
    }

    // STATS
    @GetMapping("/product/{productId}/stats")
    public ResponseEntity<?> getCommentStats(@PathVariable int productId) {

        long count = commentService.countByProductId(productId);
        double avg = commentService.getAverageRatingByProductId(productId);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Lấy thống kê bình luận thành công",
                        new Object() {
                            public final long totalComments = count;
                            public final double averageRating = avg;
                        }
                )
        );
    }
}
