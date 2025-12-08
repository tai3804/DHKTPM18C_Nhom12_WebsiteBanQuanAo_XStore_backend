package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.CommentCreateRequest;
import iuh.fit.xstore.dto.request.CommentUpdateRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.model.Comment;
import iuh.fit.xstore.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    public ResponseEntity<?> getCommentById(@PathVariable("id") int id) {
        Comment comment = commentService.findById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Lấy bình luận thành công", comment)
        );
    }

    // GET comments of product
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getCommentsByProductId(@PathVariable("productId") int productId) {
        List<Comment> comments = commentService.findByProductId(productId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Lấy bình luận sản phẩm thành công", comments)
        );
    }

    // GET comments by author
    @GetMapping("/author/{authorId}")
    public ResponseEntity<?> getCommentsByAuthorId(@PathVariable("authorId") int authorId) {
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
                request.getAuthorName(),
                request.getText(),
                request.getRate(),
                request.getImageUrls()
        );

        return ResponseEntity.status(201).body(
                new ApiResponse<>(201, "Tạo bình luận thành công", comment)
        );
    }

    // CREATE comment with multiple files upload
    @PostMapping("/with-files")
    public ResponseEntity<?> createCommentWithFiles(
            @RequestParam("productId") int productId,
            @RequestParam("authorId") int authorId,
            @RequestParam("authorName") String authorName,
            @RequestParam("text") String text,
            @RequestParam("rate") int rate,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {

        List<String> imageUrls = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    // Validate file size (10MB)
                    if (file.getSize() > 10 * 1024 * 1024) {
                        return ResponseEntity.badRequest().body(
                            new ApiResponse<>(400, "File quá lớn. Kích thước tối đa là 10MB", null)
                        );
                    }

                    // Validate file type
                    String contentType = file.getContentType();
                    if (contentType == null ||
                        (!contentType.startsWith("image/") && !contentType.startsWith("video/"))) {
                        return ResponseEntity.badRequest().body(
                            new ApiResponse<>(400, "Loại file không được hỗ trợ. Chỉ chấp nhận ảnh và video", null)
                        );
                    }

                    try {
                        // Save file and get URL
                        String imageUrl = saveCommentFile(file);
                        imageUrls.add(imageUrl);
                    } catch (Exception e) {
                        return ResponseEntity.badRequest().body(
                            new ApiResponse<>(400, "Lỗi khi lưu file: " + e.getMessage(), null)
                        );
                    }
                }
            }
        }

        Comment comment = commentService.create(productId, authorId, authorName, text, rate, imageUrls);

        return ResponseEntity.status(201).body(
                new ApiResponse<>(201, "Tạo bình luận thành công", comment)
        );
    }

    private String saveCommentFile(MultipartFile file) throws Exception {
        java.io.File dir = new java.io.File("uploads/comments");
        if (!dir.exists()) dir.mkdirs();

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = java.util.UUID.randomUUID().toString() + extension;

        // Save file
        java.nio.file.Path filePath = java.nio.file.Paths.get("uploads/comments", uniqueFilename);
        java.nio.file.Files.write(filePath, file.getBytes());

        return "/comments/" + uniqueFilename;
    }

    // UPDATE comment
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(
            @PathVariable("id") int id,
            @RequestBody CommentUpdateRequest request) {

        Comment updated = commentService.update(
                id,
                request.getText(),
                request.getRate()
        );

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cập nhật bình luận thành công", updated)
        );
    }

    // DELETE comment
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") int id) {
        commentService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Xóa bình luận thành công", null)
        );
    }

    // STATS
    @GetMapping("/product/{productId}/stats")
    public ResponseEntity<?> getCommentStats(@PathVariable("productId") int productId) {

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
