package iuh.fit.xstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private int id;
    private int authorId;
    private String authorName;
    private String text;
    private String image;
    private int rate;
    private LocalDateTime commentAt;
}
