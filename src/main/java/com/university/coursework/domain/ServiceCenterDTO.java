package com.university.coursework.domain;

import lombok.Builder;
import lombok.Value;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class ServiceCenterDTO {
    UUID centerId;
    String name;
    double rating;
    String street;
    String city;
    String region;
    String country;
    String description;
}
