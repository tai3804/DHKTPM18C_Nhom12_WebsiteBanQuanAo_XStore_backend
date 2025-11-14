package iuh.fit.xstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCreateRequest {
    private int productId; // ID sản phẩm cần comment
    private int authorId; // ID user comment

    private String text;

    private String image; // optional

    private int rate; // 1-5
}
