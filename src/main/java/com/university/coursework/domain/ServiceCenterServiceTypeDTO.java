package com.university.coursework.domain;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class ServiceCenterServiceTypeDTO {
    UUID id;
    UUID centerId;
    UUID serviceId;
    BigDecimal price;
    int duration;
}
