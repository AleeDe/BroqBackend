package com.example.shared_auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "foods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable=false, precision=10, scale=2)
    private BigDecimal price;

    private boolean available = true;

    @Column(nullable = false)
    private String category;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "food-images")
    private List<FoodImage> images = new ArrayList<>();
}
