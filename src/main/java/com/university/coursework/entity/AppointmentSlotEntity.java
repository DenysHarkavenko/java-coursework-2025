package com.university.coursework.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "appointment_slot")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "slot_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID slotId;

    @Column(name = "center_id", nullable = false)
    private UUID centerId;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;
}
