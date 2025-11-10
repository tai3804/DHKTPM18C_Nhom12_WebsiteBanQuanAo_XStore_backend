package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.service.MailService;
import iuh.fit.xstore.service.OtpMailService;
import iuh.fit.xstore.service.OtpStorageService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpMailService otpMailService;
    private final OtpStorageService otpStorageService;

//    @GetMapping("/send")
//    public ApiResponse<String> sendOtp(@RequestParam String email) throws MessagingException {
//        String otp = MailService.OtpService.generateOtp(6);
//        otpMailService.sendOtpEmail(email, otp);
//        otpStorageService.saveOtp(email, otp, 5); // OTP có hiệu lực 5 phút
//
//        return new ApiResponse<>(SuccessCode.OTP_SEND_SUCCESSFULLY, email);
//    }

    @PostMapping("/register")
    public ApiResponse<String> sendRegisterByEmailOtp(@RequestBody String email) throws MessagingException {
        // Loại bỏ dấu ngoặc kép nếu có
        String cleanEmail = email.replaceAll("^\"|\"$", "");
        
        String otp = MailService.OtpService.generateOtp(6);

        otpMailService.sendOtpEmail(cleanEmail, "đăng kí tài khoản", otp);
        otpStorageService.saveOtp(cleanEmail, otp, 5); // OTP có hiệu lực 5 phút

        return new ApiResponse<>(SuccessCode.OTP_SEND_SUCCESSFULLY, cleanEmail);
    }

    @PostMapping("/register-phone")
    public ApiResponse<String> sendRegisterByPhoneOtp(@RequestBody String phone) {
        // Loại bỏ dấu ngoặc kép nếu có
        String cleanPhone = phone.replaceAll("^\"|\"$", "");
        
        String otp = MailService.OtpService.generateOtp(6);
        
//         TODO: Thêm service gửi SMS nếu có
//         otpSmsService.sendOtpSms(cleanPhone, otp);
        
        // Lưu OTP vào storage với số điện thoại làm key, hết hạn trong 5 phút
        otpStorageService.saveOtp(cleanPhone, otp, 5);

        return new ApiResponse<>(SuccessCode.OTP_SEND_SUCCESSFULLY, cleanPhone);
    }

    @GetMapping("/verify-otp")
    public ApiResponse<String> verifyOtp(@RequestParam String contact, @RequestParam String otp) {
        System.out.println("=== OTP VERIFICATION REQUEST ===");
        System.out.println("Contact: " + contact);
        System.out.println("OTP: " + otp);
        
        boolean valid = otpStorageService.validateOtp(contact, otp);
        System.out.println("OTP Valid: " + valid);
        
        return valid ? new ApiResponse<>(SuccessCode.OTP_VALID, null) : new ApiResponse<>(ErrorCode.OTP_INVALID_OR_EXPIRATION);
    }
}
