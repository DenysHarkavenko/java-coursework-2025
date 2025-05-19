package com.university.coursework.service.impl;

import com.university.coursework.domain.AppointmentSlotDTO;
import com.university.coursework.domain.ServiceRequestDTO;
import com.university.coursework.domain.enums.ServiceRequestStatus;
import com.university.coursework.entity.ServiceRequestEntity;
import com.university.coursework.entity.UserEntity;
import com.university.coursework.event.ServiceRequestEvent;
import com.university.coursework.exception.ServiceRequestNotFoundException;
import com.university.coursework.repository.ServiceRequestRepository;
import com.university.coursework.repository.UserRepository;
import com.university.coursework.service.AppointmentSlotService;
import com.university.coursework.service.ServiceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AppointmentSlotService appointmentSlotService;
    private final ServiceRequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public ServiceRequestDTO addServiceRequest(ServiceRequestDTO requestDTO) {
        AppointmentSlotDTO appointmentSlot = appointmentSlotService.getSlotById(requestDTO.getAppointmentSlotId());
        if(!appointmentSlot.isAvailable()) {
            throw new ServiceRequestNotFoundException("Appointment slot is not available with id: " + requestDTO.getAppointmentSlotId());
        }
        ServiceRequestEntity entity = ServiceRequestEntity.builder()
                .userId(requestDTO.getUserId())
                .centerId(requestDTO.getCenterId())
                .serviceType(requestDTO.getServiceType())
                .requestDate(LocalDateTime.now())
                .status(ServiceRequestStatus.PENDING)
                .appointmentSlotId(requestDTO.getAppointmentSlotId())
                .build();
        ServiceRequestEntity savedEntity = serviceRequestRepository.save(entity);

        appointmentSlotService.updateAppointmentSlotAvailability(
                requestDTO.getAppointmentSlotId(), false);

        ServiceRequestDTO result = mapToDTO(savedEntity);

        eventPublisher.publishEvent(new ServiceRequestEvent(this, result, "CREATED"));

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceRequestDTO getServiceRequestById(UUID requestId) {
        ServiceRequestEntity entity = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new ServiceRequestNotFoundException("Request not found with id: " + requestId));
        return mapToDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceRequestDTO> getServiceRequestsByUser(UUID userId) {
        List<ServiceRequestEntity> entities = serviceRequestRepository.findByUserId(userId);
        return entities.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public ServiceRequestDTO updateServiceRequest(ServiceRequestDTO requestDTO) {
        ServiceRequestEntity entity = serviceRequestRepository.findById(requestDTO.getRequestId())
                .orElseThrow(() -> new ServiceRequestNotFoundException("Request not found with id: " + requestDTO.getRequestId()));
        entity.setServiceType(requestDTO.getServiceType());
        entity.setStatus(requestDTO.getStatus());
        entity.setAppointmentSlotId(requestDTO.getAppointmentSlotId());
        ServiceRequestEntity updatedEntity = serviceRequestRepository.save(entity);
        ServiceRequestDTO result = mapToDTO(updatedEntity);

        eventPublisher.publishEvent(new ServiceRequestEvent(this, result, "STATUS_CHANGED"));

        return result;
    }

    @Override
    public void deleteServiceRequest(UUID requestId) {
        if (!serviceRequestRepository.existsById(requestId)) {
            throw new ServiceRequestNotFoundException("Request not found with id: " + requestId);
        }
        serviceRequestRepository.deleteById(requestId);
    }

    public boolean isOwner(UUID requestId, String username) {
        return requestRepository.findById(requestId)
                .map(request -> {
                    UserEntity user = userRepository.findById(request.getUserId()).orElse(null);
                    return user != null && user.getEmail().equals(username);
                })
                .orElse(false);
    }

    private ServiceRequestDTO mapToDTO(ServiceRequestEntity entity) {
        return ServiceRequestDTO.builder()
                .requestId(entity.getRequestId())
                .userId(entity.getUserId())
                .centerId(entity.getCenterId())
                .serviceType(entity.getServiceType())
                .requestDate(entity.getRequestDate())
                .status(entity.getStatus())
                .appointmentSlotId(entity.getAppointmentSlotId())
                .build();
    }
}
