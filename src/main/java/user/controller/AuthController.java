package user.controller;

import user.dto.request.LoginRequest;
import user.dto.request.RefreshTokenRequest;
import user.dto.request.RegisterRequest;
import user.dto.response.AuthResponse;
import user.dto.response.MessageResponse;
import user.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 1. ĐĂNG KÝ
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // 2. ĐĂNG NHẬP
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // 3. REFRESH TOKEN
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    // 4. ĐĂNG XUẤT
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.logout(request));
    }
}
