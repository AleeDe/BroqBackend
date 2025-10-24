package com.example.shared_auth.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "activity_bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityBooking extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true)
    private String confirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    @JsonBackReference(value = "activity-bookings")
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-activityBookings")
    private User user;

    private Integer participants;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    private String status;

    @PrePersist
    public void prePersist() {
        if (confirmationCode == null) confirmationCode = "ACT-" + UUID.randomUUID();
    }
}
