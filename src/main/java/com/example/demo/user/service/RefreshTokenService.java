package com.example.demo.user.service;

import com.example.demo.user.storage.entity.RefreshToken;
import com.example.demo.user.storage.entity.User;
import com.example.demo.user.storage.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 1. Tạo mới hoặc làm mới Refresh Token cho User
    @Transactional
    public String createRefreshToken(User user) {
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElseGet(RefreshToken::new);

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    // 2. Tìm và xác thực tính hợp lệ / hạn sử dụng của Refresh Token
    public RefreshToken verifyExpiration(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token không tồn tại trong hệ thống!"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token đã hết hạn. Vui lòng đăng nhập lại!");
        }

        return refreshToken;
    }

    // 3. Xóa Refresh Token khi Logout
    @Transactional
    public void deleteByToken(String token) {
        if (token != null && !token.trim().isEmpty()) {
            refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
        }
    }
}
