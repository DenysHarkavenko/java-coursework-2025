package com.university.coursework.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.university.coursework.domain.ServiceTypeDTO;
import com.university.coursework.entity.ServiceTypeEntity;
import com.university.coursework.repository.ServiceTypeRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.university.coursework.service.impl.ServiceTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServiceTypeServiceImplTest {

    @Mock
    private ServiceTypeRepository repository;

    private ServiceTypeServiceImpl service;

    private UUID serviceId;
    private ServiceTypeEntity entity;
    private ServiceTypeDTO dto;

    @BeforeEach
    public void setUp() {
        service = new ServiceTypeServiceImpl(repository);

        serviceId = UUID.randomUUID();
        entity = ServiceTypeEntity.builder()
                .serviceId(serviceId)
                .name("Oil Change")
                .description("Замена масла")
                .category("Maintenance")
                .build();

        dto = ServiceTypeDTO.builder()
                .serviceId(serviceId)
                .name("Oil Change")
                .description("Замена масла")
                .category("Maintenance")
                .build();
    }

    @Test
    public void testCreateServiceType() {
        ServiceTypeDTO inputDTO = ServiceTypeDTO.builder()
                .name("Tire Rotation")
                .description("Поворот шин")
                .category("Maintenance")
                .build();

        ServiceTypeEntity savedEntity = ServiceTypeEntity.builder()
                .serviceId(UUID.randomUUID())
                .name("Tire Rotation")
                .description("Поворот шин")
                .category("Maintenance")
                .build();

        when(repository.save(any(ServiceTypeEntity.class))).thenReturn(savedEntity);

        ServiceTypeDTO result = service.createServiceType(inputDTO);

        assertNotNull(result);
        assertEquals("Tire Rotation", result.getName());
        assertEquals("Поворот шин", result.getDescription());
        assertEquals("Maintenance", result.getCategory());

        ArgumentCaptor<ServiceTypeEntity> captor = ArgumentCaptor.forClass(ServiceTypeEntity.class);
        verify(repository).save(captor.capture());
        ServiceTypeEntity captured = captor.getValue();
        assertEquals("Tire Rotation", captured.getName());
    }

    @Test
    public void testGetServiceTypeById_Success() {
        when(repository.findById(serviceId)).thenReturn(Optional.of(entity));

        ServiceTypeDTO result = service.getServiceTypeById(serviceId);

        assertNotNull(result);
        assertEquals(entity.getServiceId(), result.getServiceId());
        verify(repository).findById(serviceId);
    }

    @Test
    public void testGetServiceTypeById_NotFound() {
        when(repository.findById(serviceId)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> service.getServiceTypeById(serviceId));
        assertEquals("Service Type not found with id: " + serviceId, ex.getMessage());
    }

    @Test
    public void testGetAllServiceTypes() {
        List<ServiceTypeEntity> list = List.of(entity);
        when(repository.findAll()).thenReturn(list);

        List<ServiceTypeDTO> result = service.getAllServiceTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    public void testUpdateServiceType() {
        ServiceTypeDTO updateDTO = dto.toBuilder()
                .name("New Oil Change")
                .description("Updated description")
                .category("Service")
                .build();

        when(repository.findById(serviceId)).thenReturn(Optional.of(entity));
        when(repository.save(any(ServiceTypeEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ServiceTypeDTO result = service.updateServiceType(serviceId, updateDTO);

        assertNotNull(result);
        assertEquals("New Oil Change", result.getName());
        assertEquals("Updated description", result.getDescription());
        assertEquals("Service", result.getCategory());
        verify(repository).findById(serviceId);
        verify(repository).save(any(ServiceTypeEntity.class));
    }

    @Test
    public void testDeleteServiceType_Success() {
        when(repository.existsById(serviceId)).thenReturn(true);

        service.deleteServiceType(serviceId);

        verify(repository).existsById(serviceId);
        verify(repository).deleteById(serviceId);
    }

    @Test
    public void testDeleteServiceType_NotFound() {
        when(repository.existsById(serviceId)).thenReturn(false);

        Exception ex = assertThrows(RuntimeException.class, () -> service.deleteServiceType(serviceId));
        assertEquals("Service Type not found with id: " + serviceId, ex.getMessage());
        verify(repository).existsById(serviceId);
        verify(repository, never()).deleteById(any());
    }
}
