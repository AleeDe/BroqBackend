package com.example.shared_auth.dto;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodOrderResponse {
    private Long orderId;
    private Instant orderTime;
    private String diningMode;
    private BigDecimal totalPrice;
    private List<FoodOrderItemResponse> items;
    private String status;
}
