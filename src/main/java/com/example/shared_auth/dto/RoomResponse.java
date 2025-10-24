package com.example.shared_auth.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class RoomResponse {
    private Long id;
    private String name;
    private String type;
    private String description;
    private BigDecimal price;
    private Integer capacity;
    private List<String> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
