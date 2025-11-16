package iuh.fit.xstore.repository;

import iuh.fit.xstore.model.Chat;
import iuh.fit.xstore.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByChatRoomOrderByTimestampAsc(ChatRoom chatRoom);

    List<Chat> findByChatRoomAndIsReadFalse(ChatRoom chatRoom);

    long countByIsReadFalse();

    List<Chat> findAllByOrderByTimestampDesc();

    List<Chat> findBySenderAndIsReadFalse(Long sender);

    long countBySenderAndIsReadFalse(Long sender);
}