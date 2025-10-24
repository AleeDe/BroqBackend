package com.example.shared_auth.service;

import java.util.List;
import com.example.shared_auth.dto.UserResponse;
import org.springframework.stereotype.Service;
import com.example.shared_auth.entity.Role;
import com.example.shared_auth.entity.User;
import com.example.shared_auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authorization.AuthorizationDeniedException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    // jwtUtils removed from this service as it is not used here


    public User userCreate(User user) {
        return userRepository.save(user);
    }


    public Role getUserRole(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return user.getRole();
    }

    public Long getIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username))
                .getId();
    }

    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public UserResponse getCurrentUserDto(String username) {
        User user = getCurrentUser(username);

        UserResponse resp = mapToUserResponse(user);
        return resp;
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse r = new UserResponse();
        r.setUsername(user.getUsername());
        r.setEmail(user.getEmail());
        r.setDateOfBirth(user.getDateOfBirth());
        r.setRole(user.getRole());
        r.setActive(user.isActive());
        return r;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    
}
