package com.example.shared_auth.dto;



import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class BookingResponse {
    private Long id;
    private String bookingConfirmationCode;
    private String status;
    private BigDecimal totalPrice;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long roomId;
    private String roomName;
    private Long userId;
    private LocalDateTime createdAt;

}
