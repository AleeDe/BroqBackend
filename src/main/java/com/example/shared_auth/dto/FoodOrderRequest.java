package com.example.shared_auth.dto;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodOrderRequest {
    @NotNull(message = "Dining mode is required")
    private String diningMode;

    @NotEmpty(message = "Order items are required")
    private List<FoodOrderItemRequest> items;
}
