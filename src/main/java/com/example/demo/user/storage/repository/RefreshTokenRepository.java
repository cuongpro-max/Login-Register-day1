package com.example.demo.user.storage.repository;

import com.example.demo.user.storage.entity.RefreshToken;
import com.example.demo.user.storage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
    void deleteByToken(String token);
    void deleteByUser(User user);
}
