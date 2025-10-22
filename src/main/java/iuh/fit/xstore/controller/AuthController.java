package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.LoginRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.JwtResponse;
import iuh.fit.xstore.util.jwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest request) {
        ApiResponse<Object> response = new ApiResponse<>();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Nếu xác thực thành công → tạo JWT
            String token = jwtUtil.generateToken(request.getUsername());

            response.setCode(200);
            response.setMessage("Login successfully");
            response.setResult(new JwtResponse(token));

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            response.setCode(401);
            response.setMessage("Incorrect username or password ");
            response.setResult(null);
            return ResponseEntity.status(401).body(response);
        } catch (Exception e) {
            response.setCode(500);
            response.setMessage("System failed: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
