package com.example.shared_auth.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Data
@Getter
@Setter
public class RoomRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String type; // SINGLE, DOUBLE, SUITE, etc. (use RoomType enum values)

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    private String description;

    @Min(1)
    private Integer capacity = 1;

    // list of image URLs
    private List<@NotBlank String> images;

    
}
