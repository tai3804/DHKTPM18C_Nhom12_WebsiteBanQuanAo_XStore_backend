package iuh.fit.xstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCreateRequest {
    private int productId; // ID sản phẩm cần comment
    private int authorId; // ID user comment
    private String authorName; // Tên người comment

    private String text;

    private List<String> imageUrls; // Danh sách URL ảnh/video

    private int rate; // 1-5
}
