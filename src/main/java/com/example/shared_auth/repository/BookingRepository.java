package com.example.shared_auth.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shared_auth.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findById(Long id);
    List<Booking> findByUserId(Long userId);
    List<Booking> findByStatus(String status);
    List<Booking> findByRoomId(Long roomId);
    List<Booking> findByCheckInDate(LocalDate checkInDate);
    List<Booking> findByCheckOutDate(LocalDate checkOutDate);
    Optional<Booking> findByBookingConfirmationCode(String bookingConfirmationCode);



}