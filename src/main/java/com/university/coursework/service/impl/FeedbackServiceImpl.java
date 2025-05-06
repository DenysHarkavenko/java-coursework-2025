package com.university.coursework.service.impl;

import com.university.coursework.domain.FeedbackDTO;
import com.university.coursework.entity.FeedbackEntity;
import com.university.coursework.repository.FeedbackRepository;
import com.university.coursework.service.CenterService;
import com.university.coursework.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final CenterService centerService;

    @Override
    public FeedbackDTO submitFeedback(FeedbackDTO feedbackDTO) {
        FeedbackEntity entity = FeedbackEntity.builder()
                .userId(feedbackDTO.getUserId())
                .centerId(feedbackDTO.getCenterId())
                .rating(feedbackDTO.getRating())
                .comment(feedbackDTO.getComment())
                .createdAt(feedbackDTO.getCreatedAt() != null ? feedbackDTO.getCreatedAt() : LocalDateTime.now())
                .build();
        FeedbackEntity savedEntity = feedbackRepository.save(entity);

        centerService.updateCenterRating(feedbackDTO.getCenterId(), feedbackDTO.getRating());
        return mapToDTO(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDTO> getFeedbackByCenter(UUID centerId) {
        List<FeedbackEntity> list = feedbackRepository.findByCenterId(centerId);
        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public FeedbackDTO updateFeedback(UUID feedbackId, FeedbackDTO feedbackDTO) {
        FeedbackEntity entity = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + feedbackId));
        entity.setRating(feedbackDTO.getRating());
        entity.setComment(feedbackDTO.getComment());
        FeedbackEntity updatedEntity = feedbackRepository.save(entity);

        centerService.updateCenterRating(feedbackDTO.getCenterId(), feedbackDTO.getRating());
        return mapToDTO(updatedEntity);
    }

    @Override
    public void deleteFeedback(UUID feedbackId) {
        if (!feedbackRepository.existsById(feedbackId)) {
            throw new RuntimeException("Feedback not found with id: " + feedbackId);
        }
        feedbackRepository.deleteById(feedbackId);
    }

    private FeedbackDTO mapToDTO(FeedbackEntity entity) {
        return FeedbackDTO.builder()
                .feedbackId(entity.getFeedbackId())
                .userId(entity.getUserId())
                .centerId(entity.getCenterId())
                .rating(entity.getRating())
                .comment(entity.getComment())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
