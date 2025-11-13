package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý các callback từ các payment gateway
 * Hiện tại: CASH (thanh toán khi nhận hàng)
 * Tương lai: CARD, MOMO, ZALOPAY
 */
@RestController
@RequestMapping("/api/payment")
@AllArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * Callback từ MoMo Payment Gateway
     * POST /api/payment/momo-callback
     */
    @PostMapping("/momo-callback")
    public ResponseEntity<ApiResponse<String>> momoCallback(
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String message) {
        
        log.info("📱 MoMo Callback - OrderId: {}, TransactionId: {}, Status: {}", 
                orderId, transactionId, status);

        try {
            // TODO: Validate callback signature từ MoMo
            // if (!isValidMomoSignature(params)) { return error }

            // Xử lý MoMo callback
            if ("0".equals(status)) { // Success
                log.info("✅ MoMo payment successful for order: {}", orderId);
                return ResponseEntity.ok(new ApiResponse<>(
                        SuccessCode.PAYMENT_SUCCESSFUL.getCode(),
                        "MoMo payment successful",
                        "success"
                ));
            } else {
                log.warn("❌ MoMo payment failed: {}", message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse<>(400, "MoMo payment failed: " + message, null)
                );
            }
        } catch (Exception e) {
            log.error("❌ Error processing MoMo callback: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(500, "Error processing MoMo callback", null)
            );
        }
    }

    /**
     * Callback từ ZaloPay Payment Gateway
     * POST /api/payment/zalopay-callback
     */
    @PostMapping("/zalopay-callback")
    public ResponseEntity<ApiResponse<String>> zalopayCallback(
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String message) {
        
        log.info("📱 ZaloPay Callback - OrderId: {}, TransactionId: {}, Status: {}", 
                orderId, transactionId, status);

        try {
            // TODO: Validate callback signature từ ZaloPay
            // if (!isValidZaloPaySignature(params)) { return error }

            // Xử lý ZaloPay callback
            if ("1".equals(status)) { // Success
                log.info("✅ ZaloPay payment successful for order: {}", orderId);
                return ResponseEntity.ok(new ApiResponse<>(
                        SuccessCode.PAYMENT_SUCCESSFUL.getCode(),
                        "ZaloPay payment successful",
                        "success"
                ));
            } else {
                log.warn("❌ ZaloPay payment failed: {}", message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse<>(400, "ZaloPay payment failed: " + message, null)
                );
            }
        } catch (Exception e) {
            log.error("❌ Error processing ZaloPay callback: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(500, "Error processing ZaloPay callback", null)
            );
        }
    }

    /**
     * Redirect từ MoMo sau khi user hoàn thành thanh toán
     * GET /api/payment/momo/redirect?orderId=123&returnUrl=...
     */
    @GetMapping("/momo/redirect")
    public ResponseEntity<?> momoRedirect(
            @RequestParam int orderId,
            @RequestParam String returnUrl) {
        
        log.info("🔄 MoMo Redirect - OrderId: {}, ReturnUrl: {}", orderId, returnUrl);

        try {
            // TODO: Verify MoMo transaction status
            // boolean isPaid = momoService.verifyTransaction(orderId);
            
            // Tạm thời redirect trực tiếp
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", returnUrl + "?orderId=" + orderId)
                    .build();
        } catch (Exception e) {
            log.error("❌ Error in MoMo redirect: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error processing MoMo redirect", null));
        }
    }

    /**
     * Redirect từ ZaloPay sau khi user hoàn thành thanh toán
     * GET /api/payment/zalopay/redirect?orderId=123&returnUrl=...
     */
    @GetMapping("/zalopay/redirect")
    public ResponseEntity<?> zalopayRedirect(
            @RequestParam int orderId,
            @RequestParam String returnUrl) {
        
        log.info("🔄 ZaloPay Redirect - OrderId: {}, ReturnUrl: {}", orderId, returnUrl);

        try {
            // TODO: Verify ZaloPay transaction status
            // boolean isPaid = zalopayService.verifyTransaction(orderId);
            
            // Tạm thời redirect trực tiếp
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", returnUrl + "?orderId=" + orderId)
                    .build();
        } catch (Exception e) {
            log.error("❌ Error in ZaloPay redirect: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error processing ZaloPay redirect", null));
        }
    }

    /**
     * Health check endpoint
     * GET /api/payment/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        log.debug("🏥 Payment service health check");
        return ResponseEntity.ok(new ApiResponse<>(
                SuccessCode.SUCCESS.getCode(),
                "Payment service is running",
                "healthy"
        ));
    }

    /**
     * Webhook test endpoint
     * POST /api/payment/test-webhook
     */
    @PostMapping("/test-webhook")
    public ResponseEntity<ApiResponse<String>> testWebhook(
            @RequestBody(required = false) String payload) {
        
        log.info("🧪 Test webhook called with payload: {}", payload);
        
        return ResponseEntity.ok(new ApiResponse<>(
                SuccessCode.SUCCESS.getCode(),
                "Webhook received successfully",
                "test_ok"
        ));
    }
}
