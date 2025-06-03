package com.university.coursework.domain;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class AppointmentSlotDTO {
    UUID slotId;
    UUID centerId;
    LocalDateTime dateTime;
    boolean isAvailable;
}
