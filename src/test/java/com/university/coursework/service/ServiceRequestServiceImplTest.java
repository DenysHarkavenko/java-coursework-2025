package com.university.coursework.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.university.coursework.domain.ServiceRequestDTO;
import com.university.coursework.domain.enums.ServiceRequestStatus;
import com.university.coursework.entity.ServiceRequestEntity;
import com.university.coursework.exception.ServiceRequestNotFoundException;
import com.university.coursework.repository.ServiceRequestRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.university.coursework.service.impl.ServiceRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class ServiceRequestServiceImplTest {

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private ServiceRequestServiceImpl serviceRequestService;

    private UUID requestId;
    private UUID userId;
    private UUID centerId;
    private UUID slotId;
    private ServiceRequestEntity serviceRequestEntity;
    private ServiceRequestDTO serviceRequestDTO;

    @BeforeEach
    void setUp() {
        serviceRequestService = new ServiceRequestServiceImpl(serviceRequestRepository, eventPublisher);
        requestId = UUID.randomUUID();
        userId = UUID.randomUUID();
        centerId = UUID.randomUUID();
        slotId = UUID.randomUUID();

        serviceRequestEntity = ServiceRequestEntity.builder()
                .requestId(requestId)
                .userId(userId)
                .centerId(centerId)
                .serviceType("Oil Change")
                .requestDate(LocalDateTime.of(2025, 4, 23, 15, 30))
                .status(ServiceRequestStatus.PENDING)
                .appointmentSlotId(slotId)
                .build();

        serviceRequestDTO = ServiceRequestDTO.builder()
                .requestId(requestId)
                .userId(userId)
                .centerId(centerId)
                .serviceType("Oil Change")
                .requestDate(LocalDateTime.of(2025, 4, 23, 15, 30))
                .status(ServiceRequestStatus.PENDING)
                .appointmentSlotId(slotId)
                .build();
    }

    @Test
    public void testAddServiceRequest() {
        ServiceRequestDTO inputDTO = ServiceRequestDTO.builder()
                .userId(userId)
                .centerId(centerId)
                .serviceType("Oil Change")
                .appointmentSlotId(slotId)
                .build();

        ServiceRequestEntity savedEntity = ServiceRequestEntity.builder()
                .requestId(requestId)
                .userId(userId)
                .centerId(centerId)
                .serviceType("Oil Change")
                .requestDate(LocalDateTime.now())
                .status(ServiceRequestStatus.PENDING)
                .appointmentSlotId(inputDTO.getAppointmentSlotId())
                .build();

        when(serviceRequestRepository.save(any(ServiceRequestEntity.class))).thenReturn(savedEntity);

        ServiceRequestDTO result = serviceRequestService.addServiceRequest(inputDTO);

        assertNotNull(result);
        assertEquals(requestId, result.getRequestId());
        assertEquals(userId, result.getUserId());
        assertEquals(centerId, result.getCenterId());
        assertEquals("Oil Change", result.getServiceType());
        assertEquals(ServiceRequestStatus.PENDING, result.getStatus());
        assertNotNull(result.getRequestDate());
        assertEquals(inputDTO.getAppointmentSlotId(), result.getAppointmentSlotId());

        ArgumentCaptor<ServiceRequestEntity> captor = ArgumentCaptor.forClass(ServiceRequestEntity.class);
        verify(serviceRequestRepository).save(captor.capture());
        ServiceRequestEntity capturedEntity = captor.getValue();
        assertEquals(userId, capturedEntity.getUserId());
        assertEquals(centerId, capturedEntity.getCenterId());
        assertEquals("Oil Change", capturedEntity.getServiceType());
        assertEquals(inputDTO.getAppointmentSlotId(), capturedEntity.getAppointmentSlotId());
    }

    @Test
    public void testGetServiceRequestById_Success() {
        when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(serviceRequestEntity));

        ServiceRequestDTO result = serviceRequestService.getServiceRequestById(requestId);

        assertNotNull(result);
        assertEquals(serviceRequestEntity.getRequestId(), result.getRequestId());
        verify(serviceRequestRepository).findById(requestId);
    }

    @Test
    public void testGetServiceRequestById_NotFound() {
        when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        ServiceRequestNotFoundException ex = assertThrows(ServiceRequestNotFoundException.class,
                () -> serviceRequestService.getServiceRequestById(requestId));
        assertEquals("Request not found with id: " + requestId, ex.getMessage());
    }

    @Test
    public void testGetServiceRequestsByUser() {
        List<ServiceRequestEntity> entities = List.of(serviceRequestEntity);
        when(serviceRequestRepository.findByUserId(userId)).thenReturn(entities);

        List<ServiceRequestDTO> result = serviceRequestService.getServiceRequestsByUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(serviceRequestEntity.getUserId(), result.get(0).getUserId());
        verify(serviceRequestRepository).findByUserId(userId);
    }

    @Test
    public void testUpdateServiceRequest_Success() {
        UUID newSlotId = UUID.randomUUID();
        ServiceRequestDTO updateDTO = ServiceRequestDTO.builder()
                .requestId(requestId)
                .userId(userId)
                .centerId(centerId)
                .serviceType("Brake Check")
                .status(ServiceRequestStatus.CONFIRMED)
                .appointmentSlotId(newSlotId)
                .build();

        when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(serviceRequestEntity));
        when(serviceRequestRepository.save(any(ServiceRequestEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ServiceRequestDTO result = serviceRequestService.updateServiceRequest(updateDTO);

        assertNotNull(result);
        assertEquals("Brake Check", result.getServiceType());
        assertEquals(ServiceRequestStatus.CONFIRMED, result.getStatus());
        assertEquals(newSlotId, result.getAppointmentSlotId());
        verify(serviceRequestRepository).findById(requestId);
        verify(serviceRequestRepository).save(any(ServiceRequestEntity.class));
    }

    @Test
    public void testDeleteServiceRequest_Success() {
        when(serviceRequestRepository.existsById(requestId)).thenReturn(true);

        serviceRequestService.deleteServiceRequest(requestId);

        verify(serviceRequestRepository).existsById(requestId);
        verify(serviceRequestRepository).deleteById(requestId);
    }

    @Test
    public void testDeleteServiceRequest_NotFound() {
        when(serviceRequestRepository.existsById(requestId)).thenReturn(false);

        ServiceRequestNotFoundException ex = assertThrows(ServiceRequestNotFoundException.class,
                () -> serviceRequestService.deleteServiceRequest(requestId));
        assertEquals("Request not found with id: " + requestId, ex.getMessage());
        verify(serviceRequestRepository).existsById(requestId);
        verify(serviceRequestRepository, never()).deleteById(any());
    }
}
