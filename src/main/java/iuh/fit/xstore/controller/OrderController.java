package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.CheckoutRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Order;
import iuh.fit.xstore.model.OrderItem;
import iuh.fit.xstore.service.OrderService;
import iuh.fit.xstore.service.PaymentService;
import iuh.fit.xstore.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PdfService pdfService;

    // ========== ORDER ==========
    @GetMapping
    public ApiResponse<List<Order>> getAllOrders() {
        var orders = orderService.findAllOrders();
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), orders);
    }

    @GetMapping("/{id}")
    public ApiResponse<Order> getOrderById(@PathVariable("id") int id) {
        var order = orderService.findOrderById(id);
        if (order == null)
            return new ApiResponse<>(ErrorCode.ORDER_NOT_FOUND.getCode(),
                    ErrorCode.ORDER_NOT_FOUND.getMessage(), null);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), order);
    }

    @PostMapping
    public ApiResponse<Order> createOrder(@RequestBody Order order) {
        var created = orderService.createOrder(order);
        return new ApiResponse<>(SuccessCode.ORDER_CREATED.getCode(),
                SuccessCode.ORDER_CREATED.getMessage(), created);
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Order> updateOrderStatus(@PathVariable("id") int id, @RequestBody String status) {
        var updated = orderService.updateOrderStatus(id, status.replace("\"", "")); // Remove quotes from JSON string
        if (updated == null)
            return new ApiResponse<>(ErrorCode.ORDER_NOT_FOUND.getCode(),
                    ErrorCode.ORDER_NOT_FOUND.getMessage(), null);
        return new ApiResponse<>(SuccessCode.ORDER_UPDATED.getCode(),
                SuccessCode.ORDER_UPDATED.getMessage(), updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Integer> deleteOrder(@PathVariable("id") int id) {
        var deletedId = orderService.deleteOrder(id);
        return new ApiResponse<>(SuccessCode.ORDER_DELETED.getCode(),
                SuccessCode.ORDER_DELETED.getMessage(), deletedId);
    }

    // ========== ORDER ITEM ==========
    @GetMapping("/items")
    public ApiResponse<List<OrderItem>> getAllOrderItems() {
        var items = orderService.findAllOrderItems();
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), items);
    }

    @GetMapping("/items/{id}")
    public ApiResponse<OrderItem> getOrderItemById(@PathVariable("id") int id) {
        var item = orderService.findOrderItemById(id);
        if (item == null)
            return new ApiResponse<>(ErrorCode.ORDER_ITEM_NOT_FOUND.getCode(),
                    ErrorCode.ORDER_ITEM_NOT_FOUND.getMessage(), null);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), item);
    }

    @PostMapping("/items")
    public ApiResponse<OrderItem> createOrderItem(@RequestBody OrderItem item) {
        var created = orderService.createOrderItem(item);
        return new ApiResponse<>(SuccessCode.ORDER_ITEM_CREATED.getCode(),
                SuccessCode.ORDER_ITEM_CREATED.getMessage(), created);
    }

    @PutMapping("/items/{id}")
    public ApiResponse<OrderItem> updateOrderItem(@PathVariable("id") int id, @RequestBody OrderItem item) {
        var updated = orderService.updateOrderItem(id, item);
        if (updated == null)
            return new ApiResponse<>(ErrorCode.ORDER_ITEM_NOT_FOUND.getCode(),
                    ErrorCode.ORDER_ITEM_NOT_FOUND.getMessage(), null);
        return new ApiResponse<>(SuccessCode.ORDER_ITEM_UPDATED.getCode(),
                SuccessCode.ORDER_ITEM_UPDATED.getMessage(), updated);
    }

    @DeleteMapping("/items/{id}")
    public ApiResponse<Integer> deleteOrderItem(@PathVariable("id") int id) {
        var deletedId = orderService.deleteOrderItem(id);
        return new ApiResponse<>(SuccessCode.ORDER_ITEM_DELETED.getCode(),
                SuccessCode.ORDER_ITEM_DELETED.getMessage(), deletedId);
    }

    // ========== CHECKOUT & PAYMENT ==========
    @PostMapping("/checkout")
    public ApiResponse<Order> checkout(@RequestBody CheckoutRequest request) {
        Order order = paymentService.processCheckout(request);
        
        // Xử lý thanh toán
        if (paymentService.processPayment(order, request.getPaymentMethod())) {
            return new ApiResponse<>(SuccessCode.ORDER_CREATED.getCode(),
                    "Thanh toán thành công! Đơn hàng đã được tạo.", order);
        } else {
            return new ApiResponse<>(ErrorCode.PAYMENT_FAILED.getCode(),
                    "Thanh toán thất bại. Vui lòng thử lại.", null);
        }
    }

    @PostMapping("/{orderId}/cancel")
    public ApiResponse<String> cancelOrder(
            @PathVariable("orderId") int orderId,
            @RequestParam(defaultValue = "Khách hàng yêu cầu") String reason) {
        paymentService.cancelOrder(orderId, reason);
        return new ApiResponse<>(SuccessCode.ORDER_DELETED.getCode(),
                "Đơn hàng đã được hủy.", "success");
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Order>> getUserOrders(@PathVariable("userId") int userId) {
        List<Order> orders = paymentService.getUserOrders(userId);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), orders);
    }

    // ========== PDF EXPORT ==========
    @GetMapping("/{id}/pdf")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> downloadOrderPdf(@PathVariable("id") int id, Authentication authentication) {
        try {
            // Kiểm tra đơn hàng tồn tại
            var order = orderService.findOrderById(id);
            if (order == null) {
                return ResponseEntity.notFound().build();
            }

            // Kiểm tra quyền truy cập - chỉ chủ đơn hàng hoặc admin mới có thể tải PDF
            String currentUsername = authentication.getName();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            boolean isOrderOwner = order.getUser() != null &&
                    order.getUser().getAccount().getUsername() != null &&
                    order.getUser().getAccount().getUsername().equals(currentUsername);

            if (!isAdmin && !isOrderOwner) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            // Tạo PDF
            byte[] pdfBytes = pdfService.generateOrderPdf(order);

            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.internalServerError().build();
            }

            // Thiết lập headers cho file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "don-hang-" + id + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (IllegalArgumentException e) {
            // Lỗi tham số không hợp lệ
            System.err.println("Lỗi tham số không hợp lệ khi tạo PDF cho đơn hàng " + id + ": " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Log lỗi và trả về lỗi server
            System.err.println("Lỗi khi tạo PDF cho đơn hàng " + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}