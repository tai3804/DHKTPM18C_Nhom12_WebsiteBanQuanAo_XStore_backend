package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.LoginRequest;
import iuh.fit.xstore.dto.request.RegisterRequest;
import iuh.fit.xstore.dto.request.ResetPasswordRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Account;
import iuh.fit.xstore.model.Cart;
import iuh.fit.xstore.model.Role;
import iuh.fit.xstore.model.User;
import iuh.fit.xstore.repository.UserRepository;
import iuh.fit.xstore.security.UserDetail;
import iuh.fit.xstore.service.OtpMailService;
import iuh.fit.xstore.service.OtpStorageService;
import iuh.fit.xstore.util.JwtUtil;
import iuh.fit.xstore.util.OtpUtil;
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
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

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

        } catch (UsernameNotFoundException  e) {
            return new ApiResponse<>(ErrorCode.USER_NOT_FOUND);
        } catch (BadCredentialsException e) {
            return new ApiResponse<>(ErrorCode.INCORRECT_USERNAME_OR_PASSWORD);
        } catch (Exception e) {
            // Các lỗi không mong đợi
            e.printStackTrace();
            return new ApiResponse<>(ErrorCode.UNKNOWN_ERROR);
        }
    }

    // ================= REGISTER =================
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

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ApiResponse<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        User user = userRepository.findByAccountUsername(request.getUsername())
                .orElse(null);

        if (user == null) {
            return new ApiResponse<>(ErrorCode.USER_NOT_FOUND);
        }

        // Mã hóa mật khẩu mới
        user.getAccount().setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new ApiResponse<>(SuccessCode.RESET_PASSWORD_SUCCESSFULLY, user);
    }
}
