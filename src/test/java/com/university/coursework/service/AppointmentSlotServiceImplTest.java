package com.university.coursework.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.university.coursework.domain.AppointmentSlotDTO;
import com.university.coursework.entity.AppointmentSlotEntity;
import com.university.coursework.repository.AppointmentSlotRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.university.coursework.service.impl.AppointmentSlotServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AppointmentSlotServiceImplTest {

    @Mock
    private AppointmentSlotRepository appointmentSlotRepository;

    private AppointmentSlotServiceImpl appointmentSlotService;

    private UUID slotId;
    private UUID centerId;
    private AppointmentSlotEntity slotEntity;
    private AppointmentSlotDTO slotDTO;

    @BeforeEach
    void setUp() {
        appointmentSlotService = new AppointmentSlotServiceImpl(appointmentSlotRepository);
        slotId = UUID.randomUUID();
        centerId = UUID.randomUUID();

        slotEntity = AppointmentSlotEntity.builder()
                .slotId(slotId)
                .centerId(centerId)
                .dateTime(LocalDateTime.of(2025, 5, 1, 9, 0))
                .isAvailable(true)
                .build();

        slotDTO = AppointmentSlotDTO.builder()
                .slotId(slotId)
                .centerId(centerId)
                .dateTime(LocalDateTime.of(2025, 5, 1, 9, 0))
                .isAvailable(true)
                .build();
    }

    @Test
    public void testAddAppointmentSlot() {
        AppointmentSlotDTO inputDTO = AppointmentSlotDTO.builder()
                .centerId(centerId)
                .dateTime(LocalDateTime.of(2025, 5, 1, 9, 0))
                .isAvailable(true)
                .build();

        when(appointmentSlotRepository.save(any(AppointmentSlotEntity.class))).thenReturn(slotEntity);

        AppointmentSlotDTO result = appointmentSlotService.addAppointmentSlot(inputDTO);

        assertNotNull(result);
        assertEquals(slotId, result.getSlotId());
        assertEquals(centerId, result.getCenterId());
        assertEquals(inputDTO.getDateTime(), result.getDateTime());
        assertTrue(result.isAvailable());

        ArgumentCaptor<AppointmentSlotEntity> captor = ArgumentCaptor.forClass(AppointmentSlotEntity.class);
        verify(appointmentSlotRepository).save(captor.capture());
        AppointmentSlotEntity captured = captor.getValue();
        assertEquals(centerId, captured.getCenterId());
        assertEquals(inputDTO.getDateTime(), captured.getDateTime());
        assertTrue(captured.isAvailable());
    }

    @Test
    public void testUpdateAppointmentSlotAvailability() {
        when(appointmentSlotRepository.findById(slotId)).thenReturn(Optional.of(slotEntity));
        when(appointmentSlotRepository.save(any(AppointmentSlotEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AppointmentSlotDTO result = appointmentSlotService.updateAppointmentSlotAvailability(slotId, false);

        assertNotNull(result);
        assertEquals(slotId, result.getSlotId());
        assertFalse(result.isAvailable());
        verify(appointmentSlotRepository).findById(slotId);
        verify(appointmentSlotRepository).save(any(AppointmentSlotEntity.class));
    }

    @Test
    public void testGetAvailableSlots() {
        List<AppointmentSlotEntity> slots = List.of(slotEntity);
        when(appointmentSlotRepository.findByCenterIdAndIsAvailableTrue(centerId)).thenReturn(slots);

        List<AppointmentSlotDTO> result = appointmentSlotService.getAvailableSlots(centerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(centerId, result.get(0).getCenterId());
        verify(appointmentSlotRepository).findByCenterIdAndIsAvailableTrue(centerId);
    }
}
