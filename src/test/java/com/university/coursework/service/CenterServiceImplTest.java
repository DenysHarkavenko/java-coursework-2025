package com.university.coursework.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.university.coursework.domain.ServiceCenterDTO;
import com.university.coursework.entity.FeedbackEntity;
import com.university.coursework.entity.ServiceCenterEntity;
import com.university.coursework.exception.ServiceCenterNotFoundException;
import com.university.coursework.repository.FeedbackRepository;
import com.university.coursework.repository.ServiceCenterRepository;
import com.university.coursework.service.impl.CenterServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CenterServiceImplTest {

    @Mock
    private ServiceCenterRepository serviceCenterRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    private CenterServiceImpl centerService;

    private UUID centerId;
    private ServiceCenterEntity centerEntity;
    private ServiceCenterDTO centerDTO;

    @BeforeEach
    void setUp() {
        centerId = UUID.randomUUID();
        centerEntity = ServiceCenterEntity.builder()
                .centerId(centerId)
                .name("Auto Service")
                .rating(4.0)
                .street("Main Street")
                .city("City")
                .region("Region")
                .country("Country")
                .description("Test Description")
                .build();

        centerDTO = ServiceCenterDTO.builder()
                .centerId(centerId)
                .name("Auto Service")
                .rating(4.0)
                .street("Main Street")
                .city("City")
                .region("Region")
                .country("Country")
                .description("Test Description")
                .build();

        centerService = new CenterServiceImpl(serviceCenterRepository, feedbackRepository);
    }

    @Test
    public void testGetTopRatedCenters() {
        List<ServiceCenterEntity> centers = List.of(centerEntity);
        when(serviceCenterRepository.findAllByOrderByRatingDesc()).thenReturn(centers);

        List<ServiceCenterDTO> result = centerService.getTopRatedCenters();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        ServiceCenterDTO dto = result.get(0);
        assertEquals(centerEntity.getCenterId(), dto.getCenterId());
        verify(serviceCenterRepository).findAllByOrderByRatingDesc();
    }

    @Test
    public void testGetCenterDetails_Success() {
        when(serviceCenterRepository.findById(centerId)).thenReturn(Optional.of(centerEntity));

        ServiceCenterDTO result = centerService.getCenterById(centerId);

        assertNotNull(result);
        assertEquals(centerEntity.getName(), result.getName());
        verify(serviceCenterRepository).findById(centerId);
    }

    @Test
    public void testGetCenterDetails_NotFound() {
        when(serviceCenterRepository.findById(centerId)).thenReturn(Optional.empty());

        ServiceCenterNotFoundException exception = assertThrows(ServiceCenterNotFoundException.class,
                () -> centerService.getCenterById(centerId));
        assertEquals("Center not found with id: " + centerId, exception.getMessage());
    }

    @Test
    public void testGetCentersByRegion() {
        List<ServiceCenterEntity> centers = List.of(centerEntity);
        when(serviceCenterRepository.findByRegion("Region")).thenReturn(centers);

        List<ServiceCenterDTO> result = centerService.getCentersByRegion("Region");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Region", result.get(0).getRegion());
        verify(serviceCenterRepository).findByRegion("Region");
    }

    @Test
    public void testAddNewCenter() {
        when(serviceCenterRepository.save(any(ServiceCenterEntity.class))).thenReturn(centerEntity);

        ServiceCenterDTO result = centerService.addNewCenter(centerDTO);

        assertNotNull(result);
        assertEquals(centerDTO.getName(), result.getName());
        ArgumentCaptor<ServiceCenterEntity> captor = ArgumentCaptor.forClass(ServiceCenterEntity.class);
        verify(serviceCenterRepository).save(captor.capture());
        ServiceCenterEntity savedEntity = captor.getValue();
        assertEquals(centerDTO.getName(), savedEntity.getName());
    }

    @Test
    public void testUpdateCenter() {
        ServiceCenterDTO updateDTO = ServiceCenterDTO.builder()
                .centerId(centerId)
                .name("Updated Service")
                .rating(centerEntity.getRating())
                .street("New Street")
                .city("New City")
                .region("New Region")
                .country("New Country")
                .description("Updated Description")
                .build();

        when(serviceCenterRepository.findById(centerId)).thenReturn(Optional.of(centerEntity));
        when(serviceCenterRepository.save(any(ServiceCenterEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ServiceCenterDTO result = centerService.updateCenter(centerId, updateDTO);

        assertNotNull(result);
        assertEquals("Updated Service", result.getName());
        assertEquals("New Street", result.getStreet());
        assertEquals("New City", result.getCity());
        assertEquals("New Region", result.getRegion());
        assertEquals("New Country", result.getCountry());
        assertEquals("Updated Description", result.getDescription());
        verify(serviceCenterRepository).findById(centerId);
        verify(serviceCenterRepository).save(any(ServiceCenterEntity.class));
    }

    @Test
    public void testUpdateCenterRating() {
        FeedbackEntity feedback1 = FeedbackEntity.builder()
                .feedbackId(UUID.randomUUID())
                .centerId(centerId)
                .rating(5)
                .createdAt(LocalDateTime.now())
                .build();
        FeedbackEntity feedback2 = FeedbackEntity.builder()
                .feedbackId(UUID.randomUUID())
                .centerId(centerId)
                .rating(3)
                .createdAt(LocalDateTime.now())
                .build();
        List<FeedbackEntity> feedbacks = List.of(feedback1, feedback2);

        when(feedbackRepository.findByCenterId(centerId)).thenReturn(feedbacks);
        when(serviceCenterRepository.findById(centerId)).thenReturn(Optional.of(centerEntity));
        when(serviceCenterRepository.save(any(ServiceCenterEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ServiceCenterDTO result = centerService.updateCenterRating(centerId, 4.0);

        assertNotNull(result);
        assertEquals(4.0, result.getRating());
        verify(feedbackRepository).findByCenterId(centerId);
        verify(serviceCenterRepository).findById(centerId);
        verify(serviceCenterRepository).save(any(ServiceCenterEntity.class));
    }

    @Test
    public void testDeleteCenter_Success() {
        when(serviceCenterRepository.existsById(centerId)).thenReturn(true);

        centerService.deleteCenter(centerId);

        verify(serviceCenterRepository).existsById(centerId);
        verify(serviceCenterRepository).deleteById(centerId);
    }

    @Test
    public void testDeleteCenter_NotFound() {
        when(serviceCenterRepository.existsById(centerId)).thenReturn(false);

        ServiceCenterNotFoundException exception = assertThrows(ServiceCenterNotFoundException.class,
                () -> centerService.deleteCenter(centerId));
        assertEquals("Center not found with id: " + centerId, exception.getMessage());
        verify(serviceCenterRepository).existsById(centerId);
        verify(serviceCenterRepository, never()).deleteById(any());
    }
}
