package com.university.coursework.entity;

import com.university.coursework.domain.enums.ServiceRequestStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "service_request")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "request_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID requestId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "center_id", nullable = false)
    private UUID centerId;

    @Column(name = "service_type", nullable = false, length = 100)
    private String serviceType;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ServiceRequestStatus status;

    @Column(name = "slot_id", nullable = false, columnDefinition = "uuid")
    private UUID appointmentSlotId;
}
