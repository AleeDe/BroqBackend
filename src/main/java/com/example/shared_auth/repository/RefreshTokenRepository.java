package com.example.shared_auth.repository;

import com.example.shared_auth.entity.RefreshToken;
import com.example.shared_auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findAllByUserAndRevokedFalse(User user);
    int deleteAllByUser(User user);
}
