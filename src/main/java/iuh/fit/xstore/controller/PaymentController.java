package iuh.fit.xstore.controller;

import iuh.fit.xstore.config.VNPayConfig;
import iuh.fit.xstore.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_OrderInfo = (String) requestBody.get("vnp_OrderInfo");
            String orderType = (String) requestBody.get("ordertype");
            String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
            String vnp_IpAddr = VNPayConfig.getIpAddress(request);
            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

            int amount = ((Number) requestBody.get("amount")).intValue() * 100;
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");

            String bank_code = (String) requestBody.get("bankcode");
            if (bank_code != null && !bank_code.isEmpty()) {
                vnp_Params.put("vnp_BankCode", bank_code);
            }
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", orderType);

            String locate = (String) requestBody.get("language");
            if (locate != null && !locate.isEmpty()) {
                vnp_Params.put("vnp_Locale", locate);
            } else {
                vnp_Params.put("vnp_Locale", "vn");
            }
            vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_Returnurl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            // Billing (optional)
            if (requestBody.containsKey("txt_billing_mobile")) {
                vnp_Params.put("vnp_Bill_Mobile", (String) requestBody.get("txt_billing_mobile"));
            }
            if (requestBody.containsKey("txt_billing_email")) {
                vnp_Params.put("vnp_Bill_Email", (String) requestBody.get("txt_billing_email"));
            }
            String fullName = (String) requestBody.get("txt_billing_fullname");
            if (fullName != null && !fullName.trim().isEmpty()) {
                fullName = fullName.trim();
                int idx = fullName.indexOf(' ');
                if (idx > 0) {
                    String firstName = fullName.substring(0, idx);
                    String lastName = fullName.substring(fullName.lastIndexOf(' ') + 1);
                    vnp_Params.put("vnp_Bill_FirstName", firstName);
                    vnp_Params.put("vnp_Bill_LastName", lastName);
                }
            }
            // Add other billing fields if needed

            // Build data to hash and querystring
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

            Map<String, Object> response = new HashMap<>();
            response.put("code", "00");
            response.put("message", "success");
            response.put("data", paymentUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", "01");
            errorResponse.put("message", "Error creating payment: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
