package com.example.shared_auth.service;

import com.example.shared_auth.entity.*;
import com.example.shared_auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user, String deviceInfo) {
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(14, ChronoUnit.DAYS))
                .deviceInfo(deviceInfo)
                .revoked(false)
                .createdAt(Instant.now())
                .build();

        return refreshTokenRepository.save(token);
    }

    public RefreshToken validateAndReturn(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(t -> !t.isRevoked())
                .filter(t -> t.getExpiryDate().isAfter(Instant.now()))
                .orElse(null);
    }

    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(t -> {
            t.setRevoked(true);
            refreshTokenRepository.save(t);
        });
    }
}
