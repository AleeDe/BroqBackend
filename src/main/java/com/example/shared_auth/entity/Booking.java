package com.example.shared_auth.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bookings", indexes = {@Index(name = "idx_booking_code", columnList = "booking_confirmation_code")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "booking_confirmation_code", nullable = false, unique = true, length = 100)
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonBackReference(value = "room-bookings")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-bookings")
    private User user;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    private Integer guests;

    @Column(nullable = false, length = 20)
    private String status;

    // store price at booking time for audit
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
}
