package com.university.coursework.domain;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class NotificationDTO {
    UUID notificationId;
    UUID userId;
    String message;
    LocalDateTime createdAt;
}
