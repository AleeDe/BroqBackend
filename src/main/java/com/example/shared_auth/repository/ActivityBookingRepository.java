package com.example.shared_auth.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shared_auth.entity.ActivityBooking;

import java.util.List;
import java.util.Optional;

public interface ActivityBookingRepository extends JpaRepository<ActivityBooking, Long> {
    Optional<ActivityBooking> findByConfirmationCode(String confirmationCode);
    List<ActivityBooking> findByUserId(Long userId);
    List<ActivityBooking> findByActivityId(Long activityId);
}
