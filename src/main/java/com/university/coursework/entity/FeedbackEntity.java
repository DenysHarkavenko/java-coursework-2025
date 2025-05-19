package com.university.coursework.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "feedback")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "feedback_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID feedbackId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "center_id", nullable = false)
    private UUID centerId;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
