package com.university.coursework.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.university.coursework.domain.FeedbackDTO;
import com.university.coursework.entity.FeedbackEntity;
import com.university.coursework.repository.FeedbackRepository;
import com.university.coursework.service.CenterService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.university.coursework.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private CenterService centerService;

    private FeedbackServiceImpl feedbackService;

    private UUID userId;
    private UUID centerId;
    private UUID feedbackId;

    private FeedbackDTO inputFeedbackDTO;
    private FeedbackEntity storedEntity;

    @BeforeEach
    public void setUp() {
        feedbackService = new FeedbackServiceImpl(feedbackRepository, centerService);

        userId = UUID.randomUUID();
        centerId = UUID.randomUUID();
        feedbackId = UUID.randomUUID();

        inputFeedbackDTO = FeedbackDTO.builder()
                .userId(userId)
                .centerId(centerId)
                .rating(4)
                .comment("Great service!")
                .createdAt(LocalDateTime.now())
                .build();

        storedEntity = FeedbackEntity.builder()
                .feedbackId(feedbackId)
                .userId(userId)
                .centerId(centerId)
                .rating(inputFeedbackDTO.getRating())
                .comment(inputFeedbackDTO.getComment())
                .createdAt(inputFeedbackDTO.getCreatedAt())
                .build();
    }

    @Test
    public void testSubmitFeedback() {
        when(feedbackRepository.save(any(FeedbackEntity.class))).thenReturn(storedEntity);
        when(centerService.updateCenterRating(any(UUID.class), anyDouble())).thenReturn(null);

        FeedbackDTO result = feedbackService.submitFeedback(inputFeedbackDTO);

        assertNotNull(result);
        assertEquals(feedbackId, result.getFeedbackId());
        assertEquals(userId, result.getUserId());
        assertEquals(centerId, result.getCenterId());
        assertEquals(inputFeedbackDTO.getRating(), result.getRating());
        assertEquals(inputFeedbackDTO.getComment(), result.getComment());

        verify(feedbackRepository, times(1)).save(any(FeedbackEntity.class));
        verify(centerService, times(1)).updateCenterRating(centerId, inputFeedbackDTO.getRating());
    }

    @Test
    public void testGetFeedbackByCenter() {
        List<FeedbackEntity> feedbackList = List.of(storedEntity);
        when(feedbackRepository.findByCenterId(centerId)).thenReturn(feedbackList);

        List<FeedbackDTO> result = feedbackService.getFeedbackByCenter(centerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(feedbackId, result.get(0).getFeedbackId());
        verify(feedbackRepository, times(1)).findByCenterId(centerId);
    }

    @Test
    public void testUpdateFeedback() {
        FeedbackDTO updateDTO = inputFeedbackDTO.toBuilder()
                .rating(5)
                .comment("Excellent service!")
                .build();

        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(storedEntity));
        when(feedbackRepository.save(any(FeedbackEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(centerService.updateCenterRating(any(UUID.class), anyDouble())).thenReturn(null);

        FeedbackDTO result = feedbackService.updateFeedback(feedbackId, updateDTO);

        assertNotNull(result);
        assertEquals(5, result.getRating());
        assertEquals("Excellent service!", result.getComment());
        verify(feedbackRepository, times(1)).findById(feedbackId);
        verify(feedbackRepository, times(1)).save(any(FeedbackEntity.class));
        verify(centerService, times(1)).updateCenterRating(centerId, updateDTO.getRating());
    }

    @Test
    public void testDeleteFeedback_Success() {
        when(feedbackRepository.existsById(feedbackId)).thenReturn(true);

        feedbackService.deleteFeedback(feedbackId);

        verify(feedbackRepository, times(1)).existsById(feedbackId);
        verify(feedbackRepository, times(1)).deleteById(feedbackId);
    }

    @Test
    public void testDeleteFeedback_NotFound() {
        when(feedbackRepository.existsById(feedbackId)).thenReturn(false);

        Exception ex = assertThrows(RuntimeException.class, () -> feedbackService.deleteFeedback(feedbackId));
        assertEquals("Feedback not found with id: " + feedbackId, ex.getMessage());
        verify(feedbackRepository, times(1)).existsById(feedbackId);
        verify(feedbackRepository, never()).deleteById(any());
    }
}
