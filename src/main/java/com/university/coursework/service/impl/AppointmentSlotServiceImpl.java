package com.university.coursework.service.impl;

import com.university.coursework.domain.AppointmentSlotDTO;
import com.university.coursework.entity.AppointmentSlotEntity;
import com.university.coursework.repository.AppointmentSlotRepository;
import com.university.coursework.service.AppointmentSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentSlotServiceImpl implements AppointmentSlotService {

    private final AppointmentSlotRepository appointmentSlotRepository;

    @Override
    public AppointmentSlotDTO addAppointmentSlot(AppointmentSlotDTO slotDTO) {
        AppointmentSlotEntity entity = AppointmentSlotEntity.builder()
                .centerId(slotDTO.getCenterId())
                .dateTime(slotDTO.getDateTime())
                .isAvailable(slotDTO.isAvailable())
                .build();
        AppointmentSlotEntity saved = appointmentSlotRepository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public AppointmentSlotDTO updateAppointmentSlotAvailability(UUID slotId, boolean isAvailable) {
        AppointmentSlotEntity entity = appointmentSlotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found with id: " + slotId));
        entity.setAvailable(isAvailable);
        AppointmentSlotEntity updated = appointmentSlotRepository.save(entity);
        return mapToDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentSlotDTO> getAvailableSlots(UUID centerId) {
        List<AppointmentSlotEntity> slots = appointmentSlotRepository.findByCenterIdAndIsAvailableTrue(centerId);
        return slots.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public AppointmentSlotDTO updateSlot(UUID slotId, AppointmentSlotDTO slotDTO) {
        AppointmentSlotEntity entity = appointmentSlotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found with id: " + slotId));
        entity.setDateTime(slotDTO.getDateTime());
        entity.setAvailable(slotDTO.isAvailable());
        AppointmentSlotEntity updated = appointmentSlotRepository.save(entity);
        return mapToDTO(updated);
    }

    @Override
    public void deleteSlot(UUID slotId) {
        if (!appointmentSlotRepository.existsById(slotId)) {
            throw new RuntimeException("Slot not found with id: " + slotId);
        }
        appointmentSlotRepository.deleteById(slotId);
    }

    private AppointmentSlotDTO mapToDTO(AppointmentSlotEntity entity) {
        return AppointmentSlotDTO.builder()
                .slotId(entity.getSlotId())
                .centerId(entity.getCenterId())
                .dateTime(entity.getDateTime())
                .isAvailable(entity.isAvailable())
                .build();
    }
}
