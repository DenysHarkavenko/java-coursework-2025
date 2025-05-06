package com.university.coursework.domain;

import com.university.coursework.domain.enums.ServiceRequestStatus;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class ServiceRequestDTO {
    UUID requestId;
    UUID userId;
    UUID centerId;
    String serviceType;
    LocalDateTime requestDate;
    ServiceRequestStatus status;
    UUID appointmentSlotId;
}
