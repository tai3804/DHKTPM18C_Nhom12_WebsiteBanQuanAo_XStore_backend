package iuh.fit.xstore.controller;

import iuh.fit.xstore.service.ChatAIService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@AllArgsConstructor
public class ChatAIController {
    private final ChatAIService chatAIService;

    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String sessionId = request.get("sessionId");
        
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = "default";
        }
        
        return chatAIService.chat(message, sessionId);
    }
}
