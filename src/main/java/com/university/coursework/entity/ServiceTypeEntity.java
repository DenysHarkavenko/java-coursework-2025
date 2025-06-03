package com.university.coursework.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "service_type")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "service_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID serviceId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", length = 50)
    private String category;
}
