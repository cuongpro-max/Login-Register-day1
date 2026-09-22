package com.example.demo.user.service;

import com.example.demo.user.dto.response.UserProfileResponse;
import com.example.demo.jwt.JwtService;
import com.example.demo.user.storage.entity.User;
import com.example.demo.user.storage.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public UserProfileResponse getCurrentUserProfile(String authHeader) {
        if (authHeader == null || authHeader.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Thiếu Header Authorization!");
        }

        String token = authHeader.trim();

        // Xử lý tiền tố Bearer
        if (token.startsWith("Bearer ") || token.startsWith("bearer ")) {
            token = token.substring(7).trim();
        }

        // Loại bỏ ký tự đặc biệt thừa nếu có
        token = token.replaceAll("^[<\"'`]+|[>\"'`]+$", "").trim();

        try {
            String username = jwtService.extractUsername(token);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"));

            return new UserProfileResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getName(),
                    user.getPhone()
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access Token không hợp lệ hoặc đã hết hạn!");
        }
    }
}
