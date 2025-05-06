package com.university.coursework.service.impl;

import com.university.coursework.domain.ServiceCenterDTO;
import com.university.coursework.entity.FeedbackEntity;
import com.university.coursework.entity.ServiceCenterEntity;
import com.university.coursework.exception.ServiceCenterNotFoundException;
import com.university.coursework.repository.FeedbackRepository;
import com.university.coursework.repository.ServiceCenterRepository;
import com.university.coursework.service.CenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CenterServiceImpl implements CenterService {

    private final ServiceCenterRepository serviceCenterRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ServiceCenterDTO> getTopRatedCenters() {
        List<ServiceCenterEntity> centers = serviceCenterRepository.findAllByOrderByRatingDesc();
        return centers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceCenterDTO> getAllCenters() {
        List<ServiceCenterEntity> centers = serviceCenterRepository.findAll();
        return centers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceCenterDTO getCenterById(UUID centerId) {
        ServiceCenterEntity center = serviceCenterRepository.findById(centerId)
                .orElseThrow(() -> new ServiceCenterNotFoundException("Center not found with id: " + centerId));
        return mapToDTO(center);
    }

    @Override
    public List<ServiceCenterDTO> getCentersByRegion(String region) {
        List<ServiceCenterEntity> centers = serviceCenterRepository.findByRegion(region);
        return centers.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public ServiceCenterDTO addNewCenter(ServiceCenterDTO data) {
        ServiceCenterEntity center = ServiceCenterEntity.builder()
                .name(data.getName())
                .rating(data.getRating())
                .street(data.getStreet())
                .city(data.getCity())
                .region(data.getRegion())
                .country(data.getCountry())
                .description(data.getDescription())
                .build();
        ServiceCenterEntity savedCenter = serviceCenterRepository.save(center);
        return mapToDTO(savedCenter);
    }

    @Override
    public ServiceCenterDTO updateCenter(UUID centerId, ServiceCenterDTO centerDTO) {
        ServiceCenterEntity center = serviceCenterRepository.findById(centerId)
                .orElseThrow(() -> new ServiceCenterNotFoundException("Center not found with id: " + centerId));

        center.setName(centerDTO.getName());
        center.setStreet(centerDTO.getStreet());
        center.setCity(centerDTO.getCity());
        center.setRegion(centerDTO.getRegion());
        center.setCountry(centerDTO.getCountry());
        center.setDescription(centerDTO.getDescription());

        ServiceCenterEntity updatedCenter = serviceCenterRepository.save(center);
        return mapToDTO(updatedCenter);
    }

    @Override
    public ServiceCenterDTO updateCenterRating(UUID centerId, double newRating) {
        List<FeedbackEntity> reviews = feedbackRepository.findByCenterId(centerId);
        double avg = reviews.stream()
                .mapToInt(FeedbackEntity::getRating)
                .average()
                .orElse(newRating);

        ServiceCenterEntity center = serviceCenterRepository.findById(centerId)
                .orElseThrow(() -> new ServiceCenterNotFoundException("Center not found with id: " + centerId));

        center.setRating(avg);
        ServiceCenterEntity updatedCenter = serviceCenterRepository.save(center);
        return mapToDTO(updatedCenter);
    }


    @Override
    public void deleteCenter(UUID centerId) {
        if (!serviceCenterRepository.existsById(centerId)) {
            throw new ServiceCenterNotFoundException("Center not found with id: " + centerId);
        }
        serviceCenterRepository.deleteById(centerId);
    }

    private ServiceCenterDTO mapToDTO(ServiceCenterEntity center) {
        return ServiceCenterDTO.builder()
                .centerId(center.getCenterId())
                .name(center.getName())
                .rating(center.getRating())
                .street(center.getStreet())
                .city(center.getCity())
                .region(center.getRegion())
                .country(center.getCountry())
                .description(center.getDescription())
                .build();
    }
}
