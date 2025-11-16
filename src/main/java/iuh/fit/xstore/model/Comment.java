package iuh.fit.xstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = {"product", "author", "attachments"})
@EqualsAndHashCode(exclude = {"product", "author", "attachments"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"productInfos", "comments", "orderItems", "stockItems"})
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnoreProperties({"comments", "shipInfos", "cart", "account", "address", "role", "rawPassword", "point", "userType", "isPhoneVerified", "dob", "email", "phone", "avatar"})
    private User author;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "comment_at", nullable = false)
    private LocalDateTime commentAt;

    @Column(nullable = false)
    private int rate;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"comment"})
    private List<CommentAttachment> attachments;

    @PrePersist
    public void prePersist() {
        if (commentAt == null) {
            commentAt = LocalDateTime.now();
        }
    }
}
