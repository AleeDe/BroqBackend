package com.example.shared_auth.dto;

import java.time.LocalDate;
import java.util.List;
import com.example.shared_auth.entity.Booking;
import com.example.shared_auth.entity.Role;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private LocalDate dateOfBirth;
    private Role role;
    private Boolean active;
    private List<Booking> bookings;
}
