package com.example.shared_auth.controller;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.shared_auth.dto.BookingRequest;
import com.example.shared_auth.dto.BookingResponse;
import com.example.shared_auth.service.BookingService;

import java.util.List;

/**
 * Room booking endpoints: user + admin separation
 */
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final com.example.shared_auth.service.UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BookingResponse> bookRoom(
            @Valid @RequestBody BookingRequest request,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        Long userId = userService.getIdByUsername(userDetails.getUsername());
        return ResponseEntity.status(201).body(bookingService.bookRoom(request, userId));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponse>> getUserBookings(@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        Long userId = userService.getIdByUsername(userDetails.getUsername());
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }
}
