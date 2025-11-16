package iuh.fit.xstore.service;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.ai.tool.annotation.Tool;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatAIService {
    private ChatClient chatClient;
    private final RestTemplate restTemplate;
    
    // Memory storage for conversation history
    private final Map<String, List<Message>> conversationMemory = new ConcurrentHashMap<>();
    private static final int MAX_MEMORY_SIZE = 20; // Maximum messages per session

    public ChatAIService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
        this.restTemplate = new RestTemplate();
    }

    public String chat(String message, String sessionId) {
        String lowerMessage = message.toLowerCase();
        
        // Xử lý câu hỏi về số lượng sản phẩm
        if (lowerMessage.contains("bao nhiêu") && 
            (lowerMessage.contains("sản phẩm") || lowerMessage.contains("sản phầm") || lowerMessage.contains("sp"))) {
            int count = getProductsCount();
            String response = "Cửa hàng XStore hiện có " + count + " sản phẩm đang kinh doanh.";
            addToMemory(sessionId, new UserMessage(message), new org.springframework.ai.chat.messages.AssistantMessage(response));
            return response;
        }
        
        // Xử lý câu hỏi về danh mục
        if (lowerMessage.contains("danh mục") || lowerMessage.contains("loại") || lowerMessage.contains("category")) {
            List<?> types = getProductTypes();
            String response;
            if (types != null && !types.isEmpty()) {
                response = "Cửa hàng XStore có các danh mục sản phẩm: " + types.toString().replace("[", "").replace("]", "");
            } else {
                response = "Xin lỗi, hiện tại tôi không thể tải thông tin danh mục sản phẩm.";
            }
            addToMemory(sessionId, new UserMessage(message), new org.springframework.ai.chat.messages.AssistantMessage(response));
            return response;
        }
        
        // Xử lý câu hỏi về khuyến mãi
        if (lowerMessage.contains("khuyến mãi") || lowerMessage.contains("giảm giá") || lowerMessage.contains("sale")) {
            List<?> discounts = getDiscounts();
            String response;
            if (discounts != null && !discounts.isEmpty()) {
                response = "Hiện tại cửa hàng có " + discounts.size() + " chương trình khuyến mãi đang diễn ra.";
            } else {
                response = "Hiện tại cửa hàng không có chương trình khuyến mãi nào.";
            }
            addToMemory(sessionId, new UserMessage(message), new org.springframework.ai.chat.messages.AssistantMessage(response));
            return response;
        }
        
        // Xử lý câu hỏi về liệt kê sản phẩm
        if (lowerMessage.contains("liệt kê") || lowerMessage.contains("danh sách") || 
            lowerMessage.contains("tất cả") || lowerMessage.contains("toàn bộ")) {
            if (lowerMessage.contains("sản phẩm") || lowerMessage.contains("sp")) {
                List<?> products = getAllProducts();
                String response;
                if (products != null && !products.isEmpty()) {
                    StringBuilder responseBuilder = new StringBuilder("Danh sách sản phẩm của XStore:\n");
                    int count = 0;
                    for (Object product : products) {
                        if (count >= 10) { // Giới hạn hiển thị 10 sản phẩm đầu
                            responseBuilder.append("... và ").append(products.size() - 10).append(" sản phẩm khác.\n");
                            break;
                        }
                        if (product instanceof Map) {
                            Map<?, ?> prodMap = (Map<?, ?>) product;
                            Object name = prodMap.get("name");
                            Object price = prodMap.get("price");
                            if (name != null) {
                                responseBuilder.append("• ").append(name);
                                if (price != null) {
                                    responseBuilder.append(" - ").append(price).append(" VND");
                                }
                                responseBuilder.append("\n");
                                count++;
                            }
                        }
                    }
                    response = responseBuilder.toString();
                } else {
                    response = "Xin lỗi, hiện tại tôi không thể tải danh sách sản phẩm.";
                }
                addToMemory(sessionId, new UserMessage(message), new org.springframework.ai.chat.messages.AssistantMessage(response));
                return response;
            }
        }
        
        // Các câu hỏi khác sử dụng AI với memory
        List<Message> conversationHistory = getConversationHistory(sessionId);
        
        SystemMessage systemMessage = new SystemMessage("""
            Bạn là XStore AI, trợ lý AI của website bán hàng XStore.
            Trả lời lịch sự, hữu ích bằng tiếng Việt.
            
            QUAN TRỌNG: Bạn có thể xem lịch sử cuộc trò chuyện trước đó để hiểu ngữ cảnh và thông tin về khách hàng.
            Nếu khách hàng đã giới thiệu tên, hãy nhớ và sử dụng tên đó trong câu trả lời.
            Hãy trả lời một cách tự nhiên và liên tục dựa trên ngữ cảnh cuộc trò chuyện.
            
            Bạn có thể sử dụng các công cụ sau để trả lời câu hỏi về sản phẩm của khách hàng:
            - getProductsCount(): Lấy số lượng sản phẩm
            - getAllProducts(): Lấy danh sách tất cả sản phẩm
            - getProductById(id): Lấy thông tin sản phẩm theo ID
            - getProductTypes(): Lấy danh sách danh mục sản phẩm
            - getDiscounts(): Lấy danh sách khuyến mãi
            - searchProducts(keyword): Tìm kiếm sản phẩm theo từ khóa
            - getProductsByType(typeId): Lấy sản phẩm theo danh mục
            - getProductDetail(productId): Lấy chi tiết sản phẩm
            - getProductComments(productId): Lấy bình luận của sản phẩm
            - getProductColors(productId): Lấy màu sắc có sẵn
            - getProductSizes(productId): Lấy kích thước có sẵn
            - getProductStocks(productId): Lấy thông tin kho
            
            Chỉ sử dụng tools khi khách hàng hỏi về thông tin sản phẩm, khuyến mãi, hoặc dữ liệu từ cửa hàng.
            Đối với câu hỏi cá nhân hoặc chào hỏi, hãy trả lời trực tiếp dựa trên ngữ cảnh cuộc trò chuyện.
        """);

        UserMessage userMessage = new UserMessage(message);

        // Tạo prompt với system message, conversation history và user message
        List<Message> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.addAll(conversationHistory);
        messages.add(userMessage);

        Prompt prompt = new Prompt(messages);

        try {
            String aiResponse = chatClient
                    .prompt(prompt)
                    .tools(getToolCallbacks())
                    .call()
                    .content();
            
            // Lưu vào memory
            addToMemory(sessionId, userMessage, new org.springframework.ai.chat.messages.AssistantMessage(aiResponse));
            
            return aiResponse;
        } catch (Exception e) {
            String errorResponse = "Xin lỗi, tôi đang gặp sự cố kỹ thuật. Vui lòng thử lại sau.";
            addToMemory(sessionId, userMessage, new org.springframework.ai.chat.messages.AssistantMessage(errorResponse));
            return errorResponse;
        }
    }

    private List<Message> getConversationHistory(String sessionId) {
        return conversationMemory.getOrDefault(sessionId, new ArrayList<>());
    }

    private void addToMemory(String sessionId, Message userMessage, Message aiMessage) {
        List<Message> history = conversationMemory.computeIfAbsent(sessionId, k -> new ArrayList<>());
        
        // Thêm user message và AI response
        history.add(userMessage);
        history.add(aiMessage);
        
        // Giới hạn số lượng messages để tránh memory overflow
        if (history.size() > MAX_MEMORY_SIZE) {
            // Giữ lại 10 messages gần nhất (5 cặp conversation)
            history = history.subList(history.size() - MAX_MEMORY_SIZE, history.size());
            conversationMemory.put(sessionId, history);
        }
    }

    public ToolCallbackProvider getToolCallbacks() {
        return MethodToolCallbackProvider.builder()
                .toolObjects(this)
                .build();
    }

    // Tool functions for AI to call
    @Tool(description = "Trả về số lượng tổng sản phẩm trong cửa hàng")
    public int getProductsCount() {
        try {
            String url = "http://localhost:8080/api/products";
            // API trả về {code, message, result: [...]}
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("result")) {
                List<?> products = (List<?>) response.get("result");
                return products != null ? products.size() : 0;
            }
            return 0;
        } catch (Exception e) {
            System.out.println("Error getting products count: " + e.getMessage());
            return 0;
        }
    }

    @Tool(description = "Trả về thông tin chi tiết của sản phẩm theo ID")
    public Object getProductById(int id) {
        try {
            String url = "http://localhost:8080/api/products/" + id;
            return restTemplate.getForObject(url, Object.class);
        } catch (Exception e) {
            return "Không tìm thấy sản phẩm với ID: " + id;
        }
    }

    @Tool(description = "Trả về danh sách tất cả sản phẩm (có thể nhiều, chỉ lấy một số đầu)")
    public List<?> getAllProducts() {
        try {
            String url = "http://localhost:8080/api/products";
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("result")) {
                return (List<?>) response.get("result");
            }
            return List.of("Không thể tải danh sách sản phẩm");
        } catch (Exception e) {
            return List.of("Không thể tải danh sách sản phẩm");
        }
    }

    @Tool(description = "Trả về danh sách các loại sản phẩm")
    public List<?> getProductTypes() {
        try {
            String url = "http://localhost:8080/api/product-types";
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("result")) {
                return (List<?>) response.get("result");
            }
            return List.of("Không thể tải danh mục sản phẩm");
        } catch (Exception e) {
            return List.of("Không thể tải danh mục sản phẩm");
        }
    }

    @Tool(description = "Trả về danh sách các chương trình khuyến mãi")
    public List<?> getDiscounts() {
        try {
            String url = "http://localhost:8080/api/discounts";
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("result")) {
                return (List<?>) response.get("result");
            }
            return List.of("Không thể tải khuyến mãi");
        } catch (Exception e) {
            return List.of("Không thể tải khuyến mãi");
        }
    }

    @Tool(description = "Trả về bình luận của sản phẩm theo ID sản phẩm")
    public List<?> getProductComments(int productId) {
        try {
            String url = "http://localhost:8080/api/comments/product/" + productId;
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("result")) {
                return (List<?>) response.get("result");
            }
            return List.of("Không thể tải bình luận cho sản phẩm ID: " + productId);
        } catch (Exception e) {
            return List.of("Không thể tải bình luận cho sản phẩm ID: " + productId);
        }
    }

    @Tool(description = "Tìm kiếm sản phẩm theo từ khóa")
    public List<?> searchProducts(String keyword) {
        try {
            String url = "http://localhost:8080/api/products/search?q=" + keyword;
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("result")) {
                return (List<?>) response.get("result");
            }
            return List.of("Không tìm thấy sản phẩm với từ khóa: " + keyword);
        } catch (Exception e) {
            return List.of("Không thể tìm kiếm sản phẩm với từ khóa: " + keyword);
        }
    }

    @Tool(description = "Lấy sản phẩm theo danh mục với ID danh mục")
    public List<?> getProductsByType(int typeId) {
        try {
            String url = "http://localhost:8080/api/products/type/" + typeId;
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("result")) {
                return (List<?>) response.get("result");
            }
            return List.of("Không tìm thấy sản phẩm trong danh mục ID: " + typeId);
        } catch (Exception e) {
            return List.of("Không thể tải sản phẩm theo danh mục ID: " + typeId);
        }
    }

    @Tool(description = "Lấy thông tin chi tiết sản phẩm bao gồm cả biến thể")
    public Object getProductDetail(int productId) {
        try {
            String url = "http://localhost:8080/api/products/" + productId;
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("result")) {
                return response.get("result");
            }
            return "Không tìm thấy sản phẩm với ID: " + productId;
        } catch (Exception e) {
            return "Không thể tải chi tiết sản phẩm ID: " + productId;
        }
    }

    @Tool(description = "Lấy danh sách màu sắc có sẵn của sản phẩm")
    public List<?> getProductColors(int productId) {
        try {
            String url = "http://localhost:8080/api/products/" + productId + "/colors";
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("result")) {
                return (List<?>) response.get("result");
            }
            return List.of("Không tìm thấy màu sắc cho sản phẩm ID: " + productId);
        } catch (Exception e) {
            return List.of("Không thể tải màu sắc cho sản phẩm ID: " + productId);
        }
    }

    @Tool(description = "Lấy danh sách kích thước có sẵn của sản phẩm")
    public List<?> getProductSizes(int productId) {
        try {
            String url = "http://localhost:8080/api/products/" + productId + "/sizes";
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("result")) {
                return (List<?>) response.get("result");
            }
            return List.of("Không tìm thấy kích thước cho sản phẩm ID: " + productId);
        } catch (Exception e) {
            return List.of("Không thể tải kích thước cho sản phẩm ID: " + productId);
        }
    }

    @Tool(description = "Lấy thông tin kho của sản phẩm")
    public List<?> getProductStocks(int productId) {
        try {
            String url = "http://localhost:8080/api/products/" + productId + "/stocks";
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("result")) {
                return (List<?>) response.get("result");
            }
            return List.of("Không tìm thấy thông tin kho cho sản phẩm ID: " + productId);
        } catch (Exception e) {
            return List.of("Không thể tải thông tin kho cho sản phẩm ID: " + productId);
        }
    }
}
