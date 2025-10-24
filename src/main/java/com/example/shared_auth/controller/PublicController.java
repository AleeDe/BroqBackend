package com.example.shared_auth.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shared_auth.dto.ActivityResponse;
import com.example.shared_auth.dto.FoodResponse;
import com.example.shared_auth.dto.RoomResponse;
import com.example.shared_auth.service.ActivityService;
import com.example.shared_auth.service.RoomService;
import com.example.shared_auth.service.impl.FoodService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final RoomService roomService;
    private final FoodService foodService;
    private final ActivityService activityService;

    /** -------------------- Rooms ENDPOINTS -------------------- **/
    @GetMapping("/all/rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.findAllRooms());
    }
    /** -------------------- Food ENDPOINTS -------------------- **/

    @GetMapping("/all/foods")
    public ResponseEntity<List<FoodResponse>> getAllFoods(
            @RequestParam(defaultValue = "true") boolean availableOnly) {
        return ResponseEntity.ok(foodService.getAllFoods(availableOnly));
    }


    /** -------------------- Activity ENDPOINTS -------------------- **/

    @GetMapping("/all/activities")
    public ResponseEntity<List<ActivityResponse>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

     /** -------------------- Booking ENDPOINTS -------------------- **/
    

    /** -------------------- User ENDPOINTS -------------------- **/



    
}
