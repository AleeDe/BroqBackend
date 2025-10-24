package com.example.shared_auth.security;

import com.example.shared_auth.entity.User;
import com.example.shared_auth.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));
        // Support single or comma-separated roles stored in the Role enum column (defensive).
        String roleStr = user.getRole() != null ? user.getRole().name() : "USER";
        String[] roles = roleStr.contains(",") ? roleStr.split("\\s*,\\s*") : new String[]{roleStr};

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .disabled(!user.isActive())
            // use roles(...) so Spring will create authorities with the ROLE_ prefix (e.g. ROLE_USER)
            .roles(roles)
            .build();
    }
}
