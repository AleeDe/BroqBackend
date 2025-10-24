package com.example.shared_auth.controller;

import com.example.shared_auth.dto.*;
import com.example.shared_auth.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    final ActivityService activityService;

    // --------- Booking Section ---------

    @PostMapping("/booking/create")
    public ResponseEntity<ActivityBookingResponse> bookActivity(
            @Valid @RequestBody ActivityBookingRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(201)
                .body(activityService.bookActivity(request, userDetails.getUsername()));
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<ActivityBookingResponse>> getUserBookings(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(activityService.getUserBookings(userDetails.getUsername()));
    }
}
