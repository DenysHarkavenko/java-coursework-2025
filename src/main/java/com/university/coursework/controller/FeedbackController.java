package com.university.coursework.controller;

import com.university.coursework.domain.FeedbackDTO;
import com.university.coursework.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback Management", description = "APIs for managing feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Submit feedback", description = "Submits new feedback for a service center. Accessible by authenticated users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Feedback submitted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public FeedbackDTO submitFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.submitFeedback(feedbackDTO);
    }

    @GetMapping("/center/{centerId}")
    @Operation(summary = "Get feedback by service center", description = "Retrieves all feedback for a specific service center. Publicly accessible.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback found"),
            @ApiResponse(responseCode = "404", description = "Service center not found")
    })
    public List<FeedbackDTO> getFeedbackByCenter(@Parameter(description = "ID of the service center") @PathVariable UUID centerId) {
        return feedbackService.getFeedbackByCenter(centerId);
    }

    @PutMapping("/{feedbackId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update feedback", description = "Updates an existing feedback. Accessible by administrators and the feedback owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Feedback not found")
    })
    public FeedbackDTO updateFeedback(
            @Parameter(description = "ID of the feedback to update") @PathVariable UUID feedbackId,
            @RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.updateFeedback(feedbackId, feedbackDTO);
    }

    @DeleteMapping("/{feedbackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete feedback", description = "Deletes a feedback. Accessible by administrators and the feedback owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Feedback deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Feedback not found")
    })
    public void deleteFeedback(@Parameter(description = "ID of the feedback to delete") @PathVariable UUID feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
    }
}
