package com.university.coursework.domain;

import lombok.Builder;
import lombok.Value;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class ServiceTypeDTO {
    UUID serviceId;
    String name;
    String description;
    String category;
}
