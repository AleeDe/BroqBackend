package com.example.shared_auth.controller;

import com.example.shared_auth.dto.*;
import com.example.shared_auth.entity.*;
import com.example.shared_auth.repository.UserRepository;
import com.example.shared_auth.security.JwtUtils;
import com.example.shared_auth.service.RefreshTokenService;
import com.example.shared_auth.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          RefreshTokenService refreshTokenService,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    // 🔹 REGISTER NEW USER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already in use");
        }

        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .dateOfBirth(LocalDate.parse(request.getDateOfBirth()))
                .role(Role.USER) // assign roles
                .active(true)
                .build();

        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully");
    }

    // 🔹 LOGIN (GET JWT + REFRESH TOKEN)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            // return 401 with a helpful JSON body instead of letting an exception propagate as 500
            return ResponseEntity.status(401).body(Map.of(
                    "error", "invalid_credentials",
                    "message", "Email or password is incorrect"
            ));
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtUtils.generateJwtToken(userDetails);
        String deviceInfo = httpRequest.getHeader("User-Agent");

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String refreshToken = refreshTokenService.createRefreshToken(user, deviceInfo).getToken();

        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .build();

        return ResponseEntity.ok(jwtResponse);
    }

    // 🔹 REFRESH TOKEN
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshToken token = refreshTokenService.validateAndReturn(request.getRefreshToken());

        if (token == null || token.isRevoked()) {
            return ResponseEntity.status(401).body("Invalid or expired refresh token");
        }

        String newAccessToken = jwtUtils.generateJwtTokenFromUsername(token.getUser().getEmail());
        User user = token.getUser();

        JwtResponse response = JwtResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.getRefreshToken())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .build();

        return ResponseEntity.ok(response);
    }

    // 🔹 LOGOUT / REVOKE TOKEN
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {
        refreshTokenService.revokeToken(request.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully");
    }
}
