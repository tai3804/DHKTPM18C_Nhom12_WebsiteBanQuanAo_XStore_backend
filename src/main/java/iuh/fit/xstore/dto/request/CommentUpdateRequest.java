package iuh.fit.xstore.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentUpdateRequest {
    private String text;
    private int rate;
    private String image; // optional
}
