package com.example.shared_auth.service;




import java.util.List;

import com.example.shared_auth.dto.BookingRequest;
import com.example.shared_auth.dto.BookingResponse;


public interface BookingService {
    BookingResponse bookRoom(BookingRequest request, Long userId);
    List<BookingResponse> getUserBookings(Long userId);
    List<BookingResponse> getAllBookings(); // admin
    void statusUpdate(Long bookingId, String status);
}
