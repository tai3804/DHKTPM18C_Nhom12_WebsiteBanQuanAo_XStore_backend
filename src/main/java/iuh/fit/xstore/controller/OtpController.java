package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.User; // Import the User model
import iuh.fit.xstore.repository.UserRepository;
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
    private final UserRepository userRepository;

    // API NÀY DÙNG CHO ĐĂNG KÝ (KHÔNG CHECK EMAIL)
    @PostMapping("/register")
    public ApiResponse<String> sendRegisterByEmailOtp(@RequestBody String email) throws MessagingException {
        String cleanEmail = email.replaceAll("^\"|\"$", "");
        String otp = MailService.OtpService.generateOtp(6);
        otpMailService.sendOtpEmail(cleanEmail, "đăng kí tài khoản", otp);
        otpStorageService.saveOtp(cleanEmail, otp, 5);
        return new ApiResponse<>(SuccessCode.OTP_SEND_SUCCESSFULLY, cleanEmail);
    }

    // API NÀY DÙNG CHO QUÊN MK (CÓ CHECK EMAIL/USERNAME)
    @PostMapping("/reset-password")
    public ApiResponse<String> sendResetPasswordOtp(@RequestBody String contact) throws MessagingException {
        // contact có thể là Email hoặc Tên tài khoản (Username)
        String cleanContact = contact.replaceAll("^\"|\"$", "");

        // 1. TÌM KIẾM USER: Thử tìm bằng Email trước, sau đó tìm bằng Username
        User user = userRepository.findByEmail(cleanContact)
                .or(() -> userRepository.findByAccountUsername(cleanContact)) // <--- THÊM LOGIC TÌM BẰNG USERNAME
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 2. Lấy EMAIL THẬT của User để gửi OTP (vì OTP chỉ gửi qua Email)
        String actualEmail = user.getEmail();

        // 3. TẠO VÀ GỬI OTP
        String otp = MailService.OtpService.generateOtp(6);
        otpMailService.sendOtpEmail(actualEmail, "đặt lại mật khẩu", otp);

        // 4. LƯU OTP: Lưu bằng cleanContact (Email hoặc Username) để khớp với bước verify sau này
        otpStorageService.saveOtp(cleanContact, otp, 5);

        return new ApiResponse<>(SuccessCode.OTP_SEND_SUCCESSFULLY, cleanContact);
    }

    // === API MỚI: DÙNG CHO QUÊN MK (CÓ CHECK SĐT) ===
    @PostMapping("/reset-password-phone")
    public ApiResponse<String> sendResetPasswordPhoneOtp(@RequestBody String phone) {
        String cleanPhone = phone.replaceAll("^\"|\"$", "");

        // 1. KIỂM TRA SĐT CÓ TỒN TẠI KHÔNG
        userRepository.findByPhone(cleanPhone)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)); // Hoặc tạo ErrorCode.PHONE_NOT_FOUND

        // 2. NẾU TỒN TẠI, GỬI OTP
        String otp = MailService.OtpService.generateOtp(6);

        // !!! GIẢ LẬP GỬI SMS BẰNG CÁCH IN RA CONSOLE !!!
        System.out.println("====== SMS OTP (SIMULATION) ======");
        System.out.println("TO: " + cleanPhone);
        System.out.println("OTP: " + otp);
        System.out.println("==================================");

        otpStorageService.saveOtp(cleanPhone, otp, 5);
        return new ApiResponse<>(SuccessCode.OTP_SEND_SUCCESSFULLY, cleanPhone);
    }

    // API này dùng cho đăng ký SĐT (KHÔNG CHECK SĐT)
    @PostMapping("/register-phone")
    public ApiResponse<String> sendRegisterByPhoneOtp(@RequestBody String phone) {
        String cleanPhone = phone.replaceAll("^\"|\"$", "");
        String otp = MailService.OtpService.generateOtp(6);
        otpStorageService.saveOtp(cleanPhone, otp, 5);
        return new ApiResponse<>(SuccessCode.OTP_SEND_SUCCESSFULLY, cleanPhone);
    }

    @GetMapping("/verify-otp")
    public ApiResponse<String> verifyOtp(@RequestParam String contact, @RequestParam String otp) {
        boolean valid = otpStorageService.validateOtp(contact, otp);
        return valid ? new ApiResponse<>(SuccessCode.OTP_VALID, null) : new ApiResponse<>(ErrorCode.OTP_INVALID_OR_EXPIRATION);
    }
}