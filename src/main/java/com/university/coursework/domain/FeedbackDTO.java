package com.university.coursework.domain;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class FeedbackDTO {
    UUID feedbackId;
    UUID userId;
    UUID centerId;
    int rating;
    String comment;
    LocalDateTime createdAt;
}
