package com.university.coursework.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.university.coursework.domain.ServiceCenterServiceTypeDTO;
import com.university.coursework.entity.ServiceCenterServiceTypeEntity;
import com.university.coursework.repository.ServiceCenterServiceTypeRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.university.coursework.service.impl.ServiceCenterServiceTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServiceCenterServiceTypeServiceImplTest {

    @Mock
    private ServiceCenterServiceTypeRepository repository;

    private ServiceCenterServiceTypeServiceImpl service;

    private UUID mappingId;
    private UUID centerId;
    private UUID serviceId;
    private ServiceCenterServiceTypeEntity entity;
    private ServiceCenterServiceTypeDTO dto;

    @BeforeEach
    public void setUp() {
        service = new ServiceCenterServiceTypeServiceImpl(repository);

        mappingId = UUID.randomUUID();
        centerId  = UUID.randomUUID();
        serviceId = UUID.randomUUID();

        entity = ServiceCenterServiceTypeEntity.builder()
                .id(mappingId)
                .centerId(centerId)
                .serviceId(serviceId)
                .price(new BigDecimal("100.00"))
                .duration(60)
                .build();

        dto = ServiceCenterServiceTypeDTO.builder()
                .id(mappingId)
                .centerId(centerId)
                .serviceId(serviceId)
                .price(new BigDecimal("100.00"))
                .duration(60)
                .build();
    }

    @Test
    public void testAddServiceToCenter() {
        ServiceCenterServiceTypeDTO inputDTO = ServiceCenterServiceTypeDTO.builder()
                .centerId(centerId)
                .serviceId(serviceId)
                .price(new BigDecimal("150.00"))
                .duration(90)
                .build();

        ServiceCenterServiceTypeEntity savedEntity = ServiceCenterServiceTypeEntity.builder()
                .id(UUID.randomUUID())
                .centerId(centerId)
                .serviceId(serviceId)
                .price(inputDTO.getPrice())
                .duration(inputDTO.getDuration())
                .build();

        when(repository.save(any(ServiceCenterServiceTypeEntity.class))).thenReturn(savedEntity);

        ServiceCenterServiceTypeDTO result = service.addServiceToCenter(inputDTO);

        assertNotNull(result);
        assertEquals(centerId, result.getCenterId());
        assertEquals(serviceId, result.getServiceId());
        assertEquals(inputDTO.getPrice(), result.getPrice());
        assertEquals(inputDTO.getDuration(), result.getDuration());

        ArgumentCaptor<ServiceCenterServiceTypeEntity> captor =
                ArgumentCaptor.forClass(ServiceCenterServiceTypeEntity.class);
        verify(repository).save(captor.capture());
        ServiceCenterServiceTypeEntity captured = captor.getValue();
        assertEquals(centerId, captured.getCenterId());
        assertEquals(serviceId, captured.getServiceId());
        assertEquals(inputDTO.getPrice(), captured.getPrice());
    }

    @Test
    public void testGetServiceMapping() {
        when(repository.findById(mappingId)).thenReturn(Optional.of(entity));

        ServiceCenterServiceTypeDTO result = service.getServiceMapping(mappingId);

        assertNotNull(result);
        assertEquals(mappingId, result.getId());
        assertEquals(centerId, result.getCenterId());
        verify(repository).findById(mappingId);
    }

    @Test
    public void testGetServicesByCenter() {
        List<ServiceCenterServiceTypeEntity> list = List.of(entity);
        when(repository.findByCenterId(centerId)).thenReturn(list);

        List<ServiceCenterServiceTypeDTO> result = service.getServicesByCenter(centerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(centerId, result.get(0).getCenterId());
        verify(repository).findByCenterId(centerId);
    }

    @Test
    public void testUpdateServiceMapping() {
        ServiceCenterServiceTypeDTO updateDTO = ServiceCenterServiceTypeDTO.builder()
                .id(mappingId)
                .centerId(centerId)
                .serviceId(serviceId)
                .price(new BigDecimal("200.00"))
                .duration(120)
                .build();

        when(repository.findById(mappingId)).thenReturn(Optional.of(entity));
        when(repository.save(any(ServiceCenterServiceTypeEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ServiceCenterServiceTypeDTO result = service.updateServiceMapping(updateDTO);

        assertNotNull(result);
        assertEquals(new BigDecimal("200.00"), result.getPrice());
        assertEquals(120, result.getDuration());
        verify(repository).findById(mappingId);
        verify(repository).save(any(ServiceCenterServiceTypeEntity.class));
    }

    @Test
    public void testDeleteServiceMapping_Success() {
        when(repository.existsById(mappingId)).thenReturn(true);

        service.deleteServiceMapping(mappingId);

        verify(repository).existsById(mappingId);
        verify(repository).deleteById(mappingId);
    }

    @Test
    public void testDeleteServiceMapping_NotFound() {
        when(repository.existsById(mappingId)).thenReturn(false);

        Exception ex = assertThrows(RuntimeException.class, () -> service.deleteServiceMapping(mappingId));
        assertEquals("Mapping not found with id: " + mappingId, ex.getMessage());
        verify(repository).existsById(mappingId);
        verify(repository, never()).deleteById(any());
    }
}
