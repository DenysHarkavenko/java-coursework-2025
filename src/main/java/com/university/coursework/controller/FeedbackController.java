package com.university.coursework.controller;

import com.university.coursework.domain.FeedbackDTO;
import com.university.coursework.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FeedbackDTO submitFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.submitFeedback(feedbackDTO);
    }

    @GetMapping("/center/{centerId}")
    public List<FeedbackDTO> getFeedbackByCenter(@PathVariable UUID centerId) {
        return feedbackService.getFeedbackByCenter(centerId);
    }

    @PutMapping("/{feedbackId}")
    public FeedbackDTO updateFeedback(@PathVariable UUID feedbackId, @RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.updateFeedback(feedbackId, feedbackDTO);
    }

    @DeleteMapping("/{feedbackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeedback(@PathVariable UUID feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
    }
}
