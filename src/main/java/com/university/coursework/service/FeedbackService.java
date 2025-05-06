package com.university.coursework.service;

import com.university.coursework.domain.FeedbackDTO;
import java.util.List;
import java.util.UUID;

public interface FeedbackService {
    FeedbackDTO submitFeedback(FeedbackDTO feedbackDTO);
    List<FeedbackDTO> getFeedbackByCenter(UUID centerId);
    FeedbackDTO updateFeedback(UUID feedbackId, FeedbackDTO feedbackDTO);
    void deleteFeedback(UUID feedbackId);
}
