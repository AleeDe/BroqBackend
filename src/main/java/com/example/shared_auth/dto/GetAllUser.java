package com.example.shared_auth.dto;

import java.time.LocalDate;
import java.util.List;


import com.example.shared_auth.entity.RefreshToken;
import com.example.shared_auth.entity.Role;

import lombok.Data;

@Data
public class GetAllUser {

    private Long id;
    private String username;
    private String email;
    private LocalDate dateOfBirth;
    private Role role;
    private List<RefreshToken> refreshTokens;
    private Boolean active;
}