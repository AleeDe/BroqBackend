package com.example.shared_auth.entity;


import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length = 20)
    private RoomType type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable=false, precision = 10, scale = 2)
    private BigDecimal price;

    private Integer capacity;

    // Additional normalized table for amenities could be added; keep as JSON or separate table depending on requirements

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "room-images")
    @Builder.Default
    private List<RoomImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "room-bookings")
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();
}
