package iuh.fit.xstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId; // null for guest

    @Column
    private String sessionId; // for guest

    @Column(nullable = false)
    private String name; // user name, "xstore" for admin, "khách vãng lai" for guest
}