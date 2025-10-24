package com.example.shared_auth.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class RefreshTokenResponse {
    private Long id;
    private String token;
    private Instant expiryDate;
    private String deviceInfo;
    private boolean revoked;
    private Instant createdAt;
}
