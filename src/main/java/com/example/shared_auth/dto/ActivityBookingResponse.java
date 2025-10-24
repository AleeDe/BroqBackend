package com.example.shared_auth.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityBookingResponse {
    private Long bookingId;
    private String confirmationCode;
    private String activityName;
    private Integer participants;
    private BigDecimal totalPrice;
    private Instant createdAt;
    private String status;
}
