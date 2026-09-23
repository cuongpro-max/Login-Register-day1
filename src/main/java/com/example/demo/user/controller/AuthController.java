package com.example.demo.user.controller;

import com.example.demo.user.dto.request.LoginRequest;
import com.example.demo.user.dto.request.RefreshTokenRequest;
import com.example.demo.user.dto.request.RegisterRequest;
import com.example.demo.user.dto.response.AuthResponse;
import com.example.demo.user.dto.response.MessageResponse;
import com.example.demo.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@Tag(name = "1. Authentication", description = "Các API xác thực tài khoản (Register, Login, Refresh Token, Logout)")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 1. ĐĂNG KÝ
    @Operation(summary = "Đăng ký tài khoản mới", description = "Truyền username, password, name, phone")
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // 2. ĐĂNG NHẬP
    @Operation(summary = "Đăng nhập", description = "Truyền username và password, trả về accessToken & refreshToken")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // 3. REFRESH TOKEN
    @Operation(summary = "Cấp lại Access Token mới", description = "Truyền refreshToken từ bảng refresh_tokens để nhận accessToken mới")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    // 4. ĐĂNG XUẤT
    @Operation(summary = "Đăng xuất", description = "Xóa refreshToken khỏi bảng refresh_tokens trong PostgreSQL")
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.logout(request));
    }
}
