package com.example.shared_auth.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityBookingRequest {

    @NotNull(message = "Activity ID is required")
    private Long activityId;

    @NotNull(message = "Number of participants is required")
    @Min(value = 1, message = "At least one participant required")
    private Integer participants;
}
