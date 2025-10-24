package com.example.shared_auth.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private List<String> imageUrls;
}
