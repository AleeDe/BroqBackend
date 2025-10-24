package com.example.shared_auth.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shared_auth.dto.ActivityBookingResponse;
import com.example.shared_auth.dto.ActivityRequest;
import com.example.shared_auth.dto.ActivityResponse;
import com.example.shared_auth.dto.BookingResponse;
import com.example.shared_auth.dto.FoodOrderRequest;
import com.example.shared_auth.dto.FoodOrderResponse;
import com.example.shared_auth.dto.FoodRequest;
import com.example.shared_auth.dto.FoodResponse;
import com.example.shared_auth.dto.RoomRequest;
import com.example.shared_auth.dto.RoomResponse;
import com.example.shared_auth.service.ActivityService;
import com.example.shared_auth.service.BookingService;
import com.example.shared_auth.service.FoodOrderService;
import com.example.shared_auth.service.RoomService;
import com.example.shared_auth.service.UserService;
import com.example.shared_auth.service.impl.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RoomService roomService;
    private final BookingService bookingService;
    private final FoodService foodService;
    private final FoodOrderService foodOrderService;
    private final ActivityService activityService;

    //// User Management Endpoints ////

    @GetMapping("users/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //// Room Management Endpoints ////

     @PostMapping("/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {
        RoomResponse created = roomService.createRoom(request);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/rooms/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomRequest request) {
        RoomResponse updated = roomService.updateRoom(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/rooms/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }


    /// Booking Management Endpoints ///
    

    @GetMapping("booking/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PutMapping("booking/{id}/{statusUpdate}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long id, @PathVariable String statusUpdate) {
        try {
            bookingService.statusUpdate(id, statusUpdate);
            return ResponseEntity.ok().build();
        } catch (java.util.NoSuchElementException | IllegalArgumentException ex) {
            return ResponseEntity.status(404)
                    .body(java.util.Collections.singletonMap("error", "Booking not found"));
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                    .body(java.util.Collections.singletonMap("error", "Unable to update booking status"));
        }
    }

    
    // --------- Admin Section Food  ---------  //

    @PostMapping("/food/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FoodResponse> createFood(@Valid @RequestBody FoodRequest request) {
        return ResponseEntity.status(201).body(foodService.createFood(request));
    }

    @PutMapping("/food/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FoodResponse> updateFood(
            @PathVariable Long id, @Valid @RequestBody FoodRequest request) {
        return ResponseEntity.ok(foodService.updateFood(id, request));
    }


    @DeleteMapping("/food/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }





    // ---------  Activity  ---------  //

    @PostMapping("/activity/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityResponse> createActivity(@Valid @RequestBody ActivityRequest request) {
        return ResponseEntity.status(201).body(activityService.createActivity(request));
    }

    @PutMapping("/activity/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityResponse> updateActivity(@PathVariable Long id, @Valid @RequestBody ActivityRequest request) {
        return ResponseEntity.ok(activityService.updateActivity(id, request));
    }

    @DeleteMapping("/activity/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/activity/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ActivityResponse>> getAllActivities() {
        return ResponseEntity.ok().body(activityService.getAllActivities());
    }

    @PutMapping("/activity/booking/{id}/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateActivityBookingStatus(
            @PathVariable Long id, @PathVariable String status) {
        try {
            activityService.updateBookingStatus(id, status);
            return ResponseEntity.ok().body("Status updated successfully");
        } catch (java.util.NoSuchElementException | IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Collections.singletonMap("error", "Booking not found or invalid status"));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", "Unable to update booking status"));
        }
    }

    @GetMapping("/activity/bookings/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ActivityBookingResponse>> getAllActivityBookings() {
        return ResponseEntity.ok().body(activityService.getAllBookings());
    }   


    // -------------------- FoodOrder ENDPOINTS -------------------- //

    @GetMapping("/food-orders/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FoodOrderResponse>> getAllFoodOrders() {
        return ResponseEntity.ok().body(foodOrderService.getAllOrders());
    }

    @PutMapping("/food-orders/{id}/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateFoodOrderStatus(
            @PathVariable Long id, @PathVariable String status) {
        try {
            foodOrderService.updateOrderStatus(id, status);
            return ResponseEntity.ok().body("Status updated successfully");
        } catch (java.util.NoSuchElementException | IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(java.util.Collections.singletonMap("error", "Order not found or invalid status"));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", "Unable to update order status"));
        }
    }
}
