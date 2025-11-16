package iuh.fit.xstore.service;

import iuh.fit.xstore.model.Chat;
import iuh.fit.xstore.model.ChatRoom;
import iuh.fit.xstore.repository.ChatRepository;
import iuh.fit.xstore.repository.ChatRoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    // Lưu tin nhắn từ user
    public Chat saveUserMessage(String sessionId, String message, Long userId, String userName) {
        ChatRoom chatRoom = getOrCreateChatRoom(userId, sessionId, userName);

        Chat chat = new Chat();
        chat.setIsRead(false);
        chat.setMessage(message);
        chat.setName(userName != null && !userName.isEmpty() ? userName : chatRoom.getName());
        chat.setSender(userId != null ? userId : 0L);
        chat.setChatRoom(chatRoom);
        chat.setTimestamp(LocalDateTime.now());

        return chatRepository.save(chat);
    }

    // Gửi tin nhắn từ admin
    public Chat sendAdminMessage(Long chatRoomId, String message) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        Chat chat = new Chat();
        chat.setIsRead(true); // Admin messages are read by default
        chat.setMessage(message);
        chat.setName("xstore");
        chat.setSender(0L);
        chat.setChatRoom(chatRoom);
        chat.setTimestamp(LocalDateTime.now());

        return chatRepository.save(chat);
    }

    // Lấy lịch sử chat của một chatRoom
    public List<Chat> getChatHistory(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        return chatRepository.findByChatRoomOrderByTimestampAsc(chatRoom);
    }

    // Lấy tất cả chatRooms (cho admin)
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    // Đánh dấu đã đọc
    public void markAsRead(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Chat not found"));
        chat.setIsRead(true);
        chatRepository.save(chat);
    }

    // Đếm số tin nhắn chưa đọc
    public long getUnreadCount() {
        return chatRepository.countByIsReadFalse();
    }

    // Lấy tin nhắn chưa đọc của một chatRoom
    public List<Chat> getUnreadMessages(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        return chatRepository.findByChatRoomAndIsReadFalse(chatRoom);
    }

    // Lấy tất cả messages
    public List<Chat> getAllMessages() {
        return chatRepository.findAllByOrderByTimestampDesc();
    }

    // Lấy messages của một user (by sender)
    public List<Chat> getUserMessages(Long userId) {
        return chatRepository.findBySenderAndIsReadFalse(userId);
    }

    // Đánh dấu tất cả messages của một chatRoom là đã đọc
    public void markChatRoomMessagesAsRead(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        List<Chat> unreadMessages = chatRepository.findByChatRoomAndIsReadFalse(chatRoom);
        for (Chat chat : unreadMessages) {
            chat.setIsRead(true);
        }
        chatRepository.saveAll(unreadMessages);
    }

    // Đếm số messages chưa đọc của một chatRoom
    public long getChatRoomUnreadCount(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        return chatRepository.findByChatRoomAndIsReadFalse(chatRoom).size();
    }

    // Lấy ChatRoom theo userId
    public ChatRoom getChatRoomByUserId(Long userId) {
        return chatRoomRepository.findByUserId(userId).orElse(null);
    }

    private ChatRoom getOrCreateChatRoom(Long userId, String sessionId, String userName) {
        Optional<ChatRoom> chatRoomOpt;
        if (userId != null) {
            chatRoomOpt = chatRoomRepository.findByUserId(userId);
            if (chatRoomOpt.isEmpty()) {
                ChatRoom newRoom = new ChatRoom();
                newRoom.setUserId(userId);
                newRoom.setName(userName != null && !userName.isEmpty() ? userName : "khách vãng lai");
                return chatRoomRepository.save(newRoom);
            } else {
                ChatRoom existingRoom = chatRoomOpt.get();
                if (userName != null && !userName.isEmpty() && !userName.equals(existingRoom.getName())) {
                    existingRoom.setName(userName);
                    chatRoomRepository.save(existingRoom);
                }
                return existingRoom;
            }
        } else {
            chatRoomOpt = chatRoomRepository.findBySessionId(sessionId);
            if (chatRoomOpt.isEmpty()) {
                ChatRoom newRoom = new ChatRoom();
                newRoom.setSessionId(sessionId);
                newRoom.setName("khách vãng lai");
                return chatRoomRepository.save(newRoom);
            }
        }
        return chatRoomOpt.get();
    }
}