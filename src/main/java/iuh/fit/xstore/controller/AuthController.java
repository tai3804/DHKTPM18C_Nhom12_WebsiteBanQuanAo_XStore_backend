package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.LoginRequest;
import iuh.fit.xstore.dto.request.RegisterRequest;
import iuh.fit.xstore.dto.request.ResetPasswordRequest;
import iuh.fit.xstore.dto.request.SendOtpRequest;
import iuh.fit.xstore.dto.request.VerifyOtpRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Account;
import iuh.fit.xstore.model.Cart;
import iuh.fit.xstore.model.Role;
import iuh.fit.xstore.model.User;
import iuh.fit.xstore.repository.AccountRepository;
import iuh.fit.xstore.repository.UserRepository;
import iuh.fit.xstore.security.UserDetail;
import iuh.fit.xstore.service.JwtService;
import iuh.fit.xstore.service.OtpService;
import iuh.fit.xstore.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtUtil;
    private OtpService otpService;

    // (Giữ nguyên /login)
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetail userDetail = (UserDetail) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetail);
            User user = userRepository.getByAccountUsername(request.getUsername());
            var username = SecurityContextHolder.getContext().getAuthentication();
            log.warn("return: ", username);
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", user);
            return new ApiResponse<>(SuccessCode.LOGIN_SUCCESSFULLY, data);
        } catch (UsernameNotFoundException e) {
            return new ApiResponse<>(ErrorCode.USER_NOT_FOUND);
        } catch (BadCredentialsException e) {
            return new ApiResponse<>(ErrorCode.INCORRECT_USERNAME_OR_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(ErrorCode.UNKNOWN_ERROR);
        }
    }

    // (Giữ nguyên /register)
    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByAccountUsername(request.getUsername())) {
            return new ApiResponse<>(ErrorCode.USER_EXISTED);
        }
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dob(request.getDob())
                .account(Account.builder()
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(Role.CUSTOMER)
                        .build()
                )
                .cart(Cart.builder()
                        .total(0)
                        .build()
                )
                .build();
        userRepository.save(user);
        return new ApiResponse<>(SuccessCode.REGISTER_SUCCESSFULLY, user);
    }

    // ================= RESET PASSWORD (ĐÃ SỬA THÔNG MINH HƠN) =================
    @PostMapping("/reset-password")
    public ApiResponse<?> resetPassword(@RequestBody ResetPasswordRequest request) {

        // DTO request.getUsername() bây giờ có thể là EMAIL hoặc SĐT
        String contact = request.getUsername();

        User user = userRepository.findByEmail(contact)
                .or(() -> userRepository.findByPhone(contact)) // Thử tìm SĐT
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Account account = user.getAccount();
        if (account == null) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);

        return new ApiResponse<>(SuccessCode.RESET_PASSWORD_SUCCESSFULLY, user);
    }

    // ================= PHONE OTP VERIFICATION =================
    @PostMapping("/send-otp")
    public ApiResponse<?> sendOtp(@RequestBody SendOtpRequest request) {
        try {
            if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
                return new ApiResponse<>(ErrorCode.INVALID_INPUT);
            }

            String otp = otpService.generateOtp(request.getPhoneNumber());
            
            Map<String, Object> data = new HashMap<>();
            data.put("message", "OTP sent successfully to " + request.getPhoneNumber());
            data.put("expiryMinutes", 5);

            log.info("OTP sent to phone: {}", request.getPhoneNumber());
            return new ApiResponse<>(SuccessCode.OTP_SEND_SUCCESSFULLY, data);
        } catch (IllegalArgumentException e) {
            log.error("Invalid phone format: {}", e.getMessage());
            return new ApiResponse<>(ErrorCode.INVALID_INPUT);
        } catch (Exception e) {
            log.error("Error sending OTP: {}", e.getMessage());
            return new ApiResponse<>(ErrorCode.UNKNOWN_ERROR);
        }
    }

    @PostMapping("/verify-otp")
    public ApiResponse<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        try {
            if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
                return new ApiResponse<>(ErrorCode.INVALID_INPUT);
            }
            if (request.getOtp() == null || request.getOtp().trim().isEmpty()) {
                return new ApiResponse<>(ErrorCode.INVALID_INPUT);
            }

            // Verify OTP
            boolean isValid = otpService.verifyOtp(request.getPhoneNumber(), request.getOtp().trim());
            
            if (!isValid) {
                return new ApiResponse<>(ErrorCode.OTP_INVALID_OR_EXPIRATION);
            }

            // OTP verified - update user's phone and verification status
            // This is done when user is logged in and updates their account
            log.info("OTP verified successfully for phone: {}", request.getPhoneNumber());
            
            Map<String, Object> data = new HashMap<>();
            data.put("message", "Phone verified successfully");
            data.put("phoneNumber", request.getPhoneNumber());

            return new ApiResponse<>(SuccessCode.OTP_VALID, data);
        } catch (Exception e) {
            log.error("Error verifying OTP: {}", e.getMessage());
            return new ApiResponse<>(ErrorCode.UNKNOWN_ERROR);
        }
    }
}
