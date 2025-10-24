package com.example.shared_auth.controller;

import com.example.shared_auth.dto.*;
import com.example.shared_auth.service.FoodOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-orders")
@RequiredArgsConstructor
public class FoodOrderController {

    private final FoodOrderService foodOrderService;

    /** USER: Place food order */
    @PostMapping
    public ResponseEntity<FoodOrderResponse> createOrder(
            @Valid @RequestBody FoodOrderRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(201)
                .body(foodOrderService.createOrder(request, userDetails.getUsername()));
    }

    /** USER: Get all their orders */
    @GetMapping
    public ResponseEntity<List<FoodOrderResponse>> getUserOrders(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(foodOrderService.getUserOrders(userDetails.getUsername()));
    }

}
