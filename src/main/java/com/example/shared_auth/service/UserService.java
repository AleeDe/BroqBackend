package com.example.shared_auth.service;

import java.util.List;
import com.example.shared_auth.dto.UserResponse;
import org.springframework.stereotype.Service;
import com.example.shared_auth.entity.Role;
import com.example.shared_auth.entity.User;
import com.example.shared_auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
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
        r.setId(user.getId());
        r.setUsername(user.getUsername());
        r.setEmail(user.getEmail());
        r.setDateOfBirth(user.getDateOfBirth());
        
        r.setNumberOfBookings(user.getBookings().size());
        r.setNumberOfFoodOrders(user.getFoodOrders().size());
        r.setNumberOfActivityBookings(user.getActivityBookings().size());
        r.setProfilePictureUrl(user.getProfilePictureUrl());
        r.setNumberOfActiveBookings(user.getBookings().stream().filter(b -> b.getStatus() == "PENDING").toList().size());
        r.setNumberOfActiveActivityBookings(user.getActivityBookings().stream().filter(ab -> ab.getStatus() == "PENDING").toList().size());
        r.setNumberOfActiveDiningReservations(user.getBookings().stream().filter(b -> b.getStatus() == "PENDING").toList().size());

        return r;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    


    
}
