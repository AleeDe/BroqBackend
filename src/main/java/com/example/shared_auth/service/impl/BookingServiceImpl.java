package com.example.shared_auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shared_auth.dto.BookingRequest;
import com.example.shared_auth.dto.BookingResponse;
import com.example.shared_auth.entity.Booking;
import com.example.shared_auth.entity.Room;
import com.example.shared_auth.entity.User;
import com.example.shared_auth.exception.ResourceNotFoundException;
import com.example.shared_auth.repository.BookingRepository;
import com.example.shared_auth.repository.RoomRepository;
import com.example.shared_auth.repository.UserRepository;
import com.example.shared_auth.service.BookingService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingResponse bookRoom(BookingRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", request.getRoomId()));

        long nights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        if (nights <= 0) throw new IllegalArgumentException("Invalid check-in/check-out dates");

        // Check for overlapping bookings for the same room
        // Overlap condition: existing.checkIn < newCheckOut && existing.checkOut > newCheckIn
        List<Booking> existingBookings = bookingRepository.findByRoomId(room.getId());
        boolean overlaps = existingBookings.stream()
                .filter(b -> !b.getStatus().equals("CANCELLED")) // ignore cancelled bookings
                .anyMatch(b ->
                        b.getCheckInDate().isBefore(request.getCheckOutDate()) &&
                        b.getCheckOutDate().isAfter(request.getCheckInDate())
                );

        if (overlaps) {
            throw new IllegalStateException("Room is already booked for the selected dates");
        }

        BigDecimal total = room.getPrice().multiply(BigDecimal.valueOf(nights));

        Booking booking = Booking.builder()
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .bookingConfirmationCode("BRQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .status("PENDING")
                .totalPrice(total)
                .room(room)
                .user(user)
                .build();

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    @Override
    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private BookingResponse mapToResponse(Booking b) {
        BookingResponse r = new BookingResponse();
        r.setId(b.getId());
        r.setBookingConfirmationCode(b.getBookingConfirmationCode());
        r.setStatus(b.getStatus());
        r.setTotalPrice(b.getTotalPrice());
        r.setCheckInDate(b.getCheckInDate());
        r.setCheckOutDate(b.getCheckOutDate());
        r.setRoomId(b.getRoom().getId());
        r.setRoomName(b.getRoom().getName());
        r.setUserId(b.getUser().getId());
        r.setCreatedAt(b.getCreatedAt());
        return r;
    }

        @Override
        @Transactional
        public void statusUpdate(Long bookingId, String status) {
                Booking booking = bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId));
                if (status == null) throw new IllegalArgumentException("Status must not be null");
                booking.setStatus(status.toUpperCase());
                bookingRepository.save(booking);
}

}

