package com.university.coursework.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "service_center")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCenterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "center_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID centerId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "street", length = 200)
    private String street;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "region", nullable = false, length = 100)
    private String region;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
