package com.example.shared_auth.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private LocalDate dateOfBirth;
    private int numberOfBookings;
    private int numberOfFoodOrders;
    private int numberOfActivityBookings;
    private int numberOfActiveBookings;
    private int numberOfActiveActivityBookings;
    private int numberOfActiveDiningReservations;
    private String profilePictureUrl;
}
