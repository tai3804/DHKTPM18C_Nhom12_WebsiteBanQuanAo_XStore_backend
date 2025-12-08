package iuh.fit.xstore.controller;

import iuh.fit.xstore.model.Chat;
import iuh.fit.xstore.model.ChatRoom;
import iuh.fit.xstore.model.User;
import iuh.fit.xstore.repository.UserRepository;
import iuh.fit.xstore.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final UserRepository userRepository;

    // API cho user gửi tin nhắn
    @PostMapping("/user")
    public ResponseEntity<Chat> sendUserMessage(@RequestBody Map<String, Object> request) {
        String sessionId = (String) request.get("sessionId");
        String message = (String) request.get("message");
        Long userId = request.get("userId") != null ? Long.valueOf(request.get("userId").toString()) : null;
        String userName = (String) request.get("userName");

        if (message == null) {
            return ResponseEntity.badRequest().build();
        }

        Chat savedChat = chatService.saveUserMessage(sessionId, message, userId, userName);
        return ResponseEntity.ok(savedChat);
    }

    // API cho admin gửi tin nhắn
    @PostMapping("/admin/send/{chatRoomId}")
    public ResponseEntity<Chat> sendAdminMessage(@PathVariable("chatRoomId") Long chatRoomId, @RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null) {
            return ResponseEntity.badRequest().build();
        }

        Chat sentMessage = chatService.sendAdminMessage(chatRoomId, message);
        return ResponseEntity.ok(sentMessage);
    }

    // API lấy lịch sử chat của một chatRoom
    @GetMapping("/history/{chatRoomId}")
    public ResponseEntity<List<Chat>> getChatHistory(@PathVariable("chatRoomId") Long chatRoomId) {
        List<Chat> history = chatService.getChatHistory(chatRoomId);
        return ResponseEntity.ok(history);
    }

    // API lấy tất cả chatRooms với last message
    @GetMapping("/admin/chat-rooms")
    public ResponseEntity<List<Map<String, Object>>> getAllChatRooms() {
        List<ChatRoom> chatRooms = chatService.getAllChatRooms();
        List<Map<String, Object>> result = chatRooms.stream().map(room -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", room.getId());
            map.put("userId", room.getUserId());
            map.put("sessionId", room.getSessionId());
            map.put("name", room.getName());
            // Get unread count
            long unreadCount = chatService.getChatRoomUnreadCount(room.getId());
            map.put("unreadCount", unreadCount);
            // Get last message
            List<Chat> history = chatService.getChatHistory(room.getId());
            if (!history.isEmpty()) {
                Chat lastChat = history.get(history.size() - 1);
                Map<String, Object> lastMessageMap = new HashMap<>();
                lastMessageMap.put("message", lastChat.getMessage());
                lastMessageMap.put("timestamp", lastChat.getTimestamp());
                lastMessageMap.put("sender", lastChat.getSender());
                lastMessageMap.put("name", lastChat.getName());
                map.put("lastMessage", lastMessageMap);
            }
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // API đánh dấu đã đọc
    @PutMapping("/admin/mark-read/{chatId}")
    public ResponseEntity<Void> markAsRead(@PathVariable("chatId") Long chatId) {
        chatService.markAsRead(chatId);
        return ResponseEntity.ok().build();
    }

    // API lấy số tin nhắn chưa đọc
    @GetMapping("/admin/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        long count = chatService.getUnreadCount();
        return ResponseEntity.ok(count);
    }

    // API lấy tin nhắn chưa đọc của một chatRoom
    @GetMapping("/admin/unread/{chatRoomId}")
    public ResponseEntity<List<Chat>> getUnreadMessages(@PathVariable("chatRoomId") Long chatRoomId) {
        List<Chat> unreadMessages = chatService.getUnreadMessages(chatRoomId);
        return ResponseEntity.ok(unreadMessages);
    }

    // API lấy tất cả messages
    @GetMapping("/admin/all-messages")
    public ResponseEntity<List<Chat>> getAllMessages() {
        List<Chat> messages = chatService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    // API đánh dấu tất cả messages của một chatRoom là đã đọc
    @PutMapping("/admin/mark-read/chat-room/{chatRoomId}")
    public ResponseEntity<Void> markChatRoomMessagesAsRead(@PathVariable("chatRoomId") Long chatRoomId) {
        chatService.markChatRoomMessagesAsRead(chatRoomId);
        return ResponseEntity.ok().build();
    }

    // API lấy số messages chưa đọc của một chatRoom
    @GetMapping("/admin/chat-room/{chatRoomId}/unread-count")
    public ResponseEntity<Long> getChatRoomUnreadCount(@PathVariable("chatRoomId") Long chatRoomId) {
        long count = chatService.getChatRoomUnreadCount(chatRoomId);
        return ResponseEntity.ok(count);
    }

    // API lấy lịch sử chat cho user đã đăng nhập
    @GetMapping("/user/history")
    public ResponseEntity<List<Chat>> getUserChatHistory() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(401).build();
        }

        String username = auth.getName();
        User user = userRepository.getByAccountUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        Long userId = (long) user.getId();
        ChatRoom room = chatService.getChatRoomByUserId(userId);
        if (room == null) {
            return ResponseEntity.ok(List.of());
        }

        List<Chat> history = chatService.getChatHistory(room.getId());
        return ResponseEntity.ok(history);
    }
}