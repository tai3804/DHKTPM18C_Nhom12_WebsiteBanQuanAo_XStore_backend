package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.service.OtpMailService;
import iuh.fit.xstore.service.OtpStorageService;
import iuh.fit.xstore.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpMailService otpMailService;
    private final OtpStorageService otpStorageService;

    @GetMapping("/send")
    public ApiResponse<String> sendOtp(@RequestParam String email) throws MessagingException {
        String otp = OtpUtil.generateOtp(6);
        otpMailService.sendOtpEmail(email, otp);
        otpStorageService.saveOtp(email, otp, 5); // OTP có hiệu lực 5 phút

        return new ApiResponse<>(SuccessCode.OTP_SEND_SUCCESSFULLY, email);
    }

    @GetMapping("/register-Email")
    public ApiResponse<String> sendRegisterByEmailOtp(@RequestParam String email) throws MessagingException {
        String otp = OtpUtil.generateOtp(6);

        otpMailService.sendOtpEmail(email, otp);
        otpStorageService.saveOtp(email, otp, 5); // OTP có hiệu lực 5 phút

        return new ApiResponse<>(SuccessCode.OTP_SEND_SUCCESSFULLY, email);
    }



    @GetMapping("/verify-otp")
    public ApiResponse<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean valid = otpStorageService.validateOtp(email, otp);
        return valid ? new ApiResponse<>(SuccessCode.OTP_VALID, null) : new ApiResponse<>(ErrorCode.OTP_INVALID_OR_EXPIRATION);
    }
}
