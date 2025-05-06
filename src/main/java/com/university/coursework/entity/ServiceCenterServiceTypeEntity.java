package com.university.coursework.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "service_center_service",
        uniqueConstraints = @UniqueConstraint(name = "uq_service_center_service", columnNames = {"center_id", "service_id"}))
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCenterServiceTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "center_id", nullable = false)
    private UUID centerId;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "duration", nullable = false)
    private int duration;
}
