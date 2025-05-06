package com.university.coursework.service;

import com.university.coursework.domain.AppointmentSlotDTO;

import java.util.List;
import java.util.UUID;

public interface AppointmentSlotService {
    AppointmentSlotDTO addAppointmentSlot(AppointmentSlotDTO slotDTO);
    AppointmentSlotDTO updateAppointmentSlotAvailability(UUID slotId, boolean isAvailable);
    List<AppointmentSlotDTO> getAvailableSlots(UUID centerId);
    AppointmentSlotDTO updateSlot(UUID slotId, AppointmentSlotDTO slotDTO);
    void deleteSlot(UUID slotId);
}
