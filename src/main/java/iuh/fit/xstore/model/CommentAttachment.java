package iuh.fit.xstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment_attachments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = {"comment"})
@EqualsAndHashCode(exclude = {"comment"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CommentAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    @JsonIgnoreProperties({"attachments", "product", "author"})
    private Comment comment;

    @Column(nullable = false)
    private String imageUrl;

    @Column(name = "file_type", nullable = false)
    private String fileType; // "image" or "video"

    @Column(name = "created_at", nullable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = java.time.LocalDateTime.now();
        }
    }
}