package iuh.fit.xstore.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpStorageService {

    private final Map<String, OtpEntry> otpCache = new ConcurrentHashMap<>();

    // Lưu OTP
    public void saveOtp(String email, String otp, int expireMinutes) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(expireMinutes);
        otpCache.put(email, new OtpEntry(otp, expiryTime));
        System.out.println("=== OTP SAVED ===");
        System.out.println("Email/Contact: " + email);
        System.out.println("OTP: " + otp);
        System.out.println("Expiry: " + expiryTime);
        System.out.println("Cache size: " + otpCache.size());
    }

    // Kiểm tra OTP
    public boolean validateOtp(String email, String otp) {
        System.out.println("=== OTP VALIDATION ===");
        System.out.println("Input Email/Contact: " + email);
        System.out.println("Input OTP: " + otp);
        System.out.println("Cache size: " + otpCache.size());
        System.out.println("Cache keys: " + otpCache.keySet());
        
        OtpEntry entry = otpCache.get(email);
        if (entry == null) {
            System.out.println("No OTP entry found for: " + email);
            return false;
        }
        
        System.out.println("Found OTP: " + entry.getOtp());
        System.out.println("Expiry time: " + entry.getExpiryTime());
        System.out.println("Current time: " + LocalDateTime.now());
        
        if (entry.getExpiryTime().isBefore(LocalDateTime.now())) {
            System.out.println("OTP expired, removing from cache");
            otpCache.remove(email);
            return false;
        }
        
        boolean match = entry.getOtp().equals(otp);
        System.out.println("OTP match: " + match);
        
        if (match) {
            System.out.println("OTP validation successful, removing from cache");
            otpCache.remove(email); // OTP chỉ dùng 1 lần
        }
        
        return match;
    }

    // Class lưu OTP + thời gian hết hạn
    private static class OtpEntry {
        private final String otp;
        private final LocalDateTime expiryTime;

        public OtpEntry(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() { return otp; }
        public LocalDateTime getExpiryTime() { return expiryTime; }
    }
}
