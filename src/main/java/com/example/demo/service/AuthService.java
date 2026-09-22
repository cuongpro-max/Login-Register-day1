package com.example.demo.service;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RefreshTokenRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.entity.User;
import com.example.demo.jwt.JwtService;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
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
        
        // Băm mật khẩu bằng BCrypt: $2a$10$...
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        user.setName(request.getName());
        user.setPhone(request.getPhone());

        userRepository.save(user);

        return new MessageResponse("Đăng ký tài khoản thành công!");
    }

    // 2. Logic Đăng nhập (Kiểm tra mật khẩu băm)
    public AuthResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        // Kiểm tra user có tồn tại và password có khớp với chuỗi đã băm không
        if (userOptional.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu!");
        }

        User user = userOptional.get();

        // Tạo Access Token & Refresh Token
        String accessToken = jwtService.generateAccessToken(user.getUsername());
        String refreshToken = UUID.randomUUID().toString();

        // Lưu Refresh Token vào Database
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new AuthResponse("Đăng nhập thành công!", accessToken, refreshToken);
    }

    // 3. Logic Refresh Token
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String token = request.getRefreshToken();
        if (token == null || token.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng cung cấp refreshToken!");
        }

        User user = userRepository.findByRefreshToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token không hợp lệ hoặc đã hết hạn!"));

        String newAccessToken = jwtService.generateAccessToken(user.getUsername());

        return new AuthResponse("Cấp mới Access Token thành công!", newAccessToken, user.getRefreshToken());
    }

    // 4. Logic Đăng xuất
    public MessageResponse logout(RefreshTokenRequest request) {
        if (request.getRefreshToken() != null) {
            userRepository.findByRefreshToken(request.getRefreshToken()).ifPresent(user -> {
                user.setRefreshToken(null);
                userRepository.save(user);
            });
        }
        return new MessageResponse("Đăng xuất thành công!");
    }
}
