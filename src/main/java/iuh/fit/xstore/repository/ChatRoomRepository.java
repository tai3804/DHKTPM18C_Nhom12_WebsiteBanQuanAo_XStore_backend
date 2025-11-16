package iuh.fit.xstore.repository;

import iuh.fit.xstore.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUserId(Long userId);
    Optional<ChatRoom> findBySessionId(String sessionId);
}