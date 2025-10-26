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
    }

    // Kiểm tra OTP
    public boolean validateOtp(String email, String otp) {
        OtpEntry entry = otpCache.get(email);
        if (entry == null) return false;
        if (entry.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpCache.remove(email);
            return false;
        }
        boolean match = entry.getOtp().equals(otp);
        if (match) otpCache.remove(email); // OTP chỉ dùng 1 lần
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
