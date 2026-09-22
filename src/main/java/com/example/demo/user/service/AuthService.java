package com.example.demo.user.service;

import com.example.demo.jwt.JwtService;
import com.example.demo.user.dto.request.LoginRequest;
import com.example.demo.user.dto.request.RefreshTokenRequest;
import com.example.demo.user.dto.request.RegisterRequest;
import com.example.demo.user.dto.response.AuthResponse;
import com.example.demo.user.dto.response.MessageResponse;
import com.example.demo.user.storage.entity.RefreshToken;
import com.example.demo.user.storage.entity.User;
import com.example.demo.user.storage.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder,
                       RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    // 1. Logic Đăng ký (Băm mật khẩu trước khi lưu DB)
    public MessageResponse register(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty() ||
            request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username và password không được để trống!");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username đã tồn tại trên hệ thống!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setPhone(request.getPhone());

        userRepository.save(user);

        return new MessageResponse("Đăng ký tài khoản thành công!");
    }

    // 2. Logic Đăng nhập (Kiểm tra mật khẩu & lưu Refresh Token vào bảng riêng)
    public AuthResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        if (userOptional.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu!");
        }

        User user = userOptional.get();

        // Tạo Access Token (JWT - 15 phút)
        String accessToken = jwtService.generateAccessToken(user.getUsername());

        // Tạo & Lưu Refresh Token vào bảng refresh_tokens (hạn 7 ngày)
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse("Đăng nhập thành công!", accessToken, refreshToken);
    }

    // 3. Logic Refresh Token (Tra cứu từ bảng refresh_tokens và kiểm tra hạn dùng)
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String token = request.getRefreshToken();
        if (token == null || token.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng cung cấp refreshToken!");
        }

        RefreshToken verifiedRefreshToken = refreshTokenService.verifyExpiration(token);
        User user = verifiedRefreshToken.getUser();

        // Cấp Access Token mới
        String newAccessToken = jwtService.generateAccessToken(user.getUsername());

        return new AuthResponse("Cấp mới Access Token thành công!", newAccessToken, verifiedRefreshToken.getToken());
    }

    // 4. Logic Đăng xuất (Xóa Refresh Token khỏi bảng refresh_tokens)
    public MessageResponse logout(RefreshTokenRequest request) {
        if (request.getRefreshToken() != null) {
            refreshTokenService.deleteByToken(request.getRefreshToken());
        }
        return new MessageResponse("Đăng xuất thành công!");
    }
}
