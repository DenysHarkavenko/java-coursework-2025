package com.university.coursework.controller;

import com.university.coursework.domain.AppointmentSlotDTO;
import com.university.coursework.service.AppointmentSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/slots")
@RequiredArgsConstructor
public class AppointmentSlotController {

    private final AppointmentSlotService appointmentSlotService;

    @GetMapping("/center/{centerId}")
    public List<AppointmentSlotDTO> getAvailableSlots(@PathVariable UUID centerId) {
        return appointmentSlotService.getAvailableSlots(centerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentSlotDTO addSlot(@RequestBody AppointmentSlotDTO slotDTO) {
        return appointmentSlotService.addAppointmentSlot(slotDTO);
    }

    @PutMapping("/{slotId}")
    public AppointmentSlotDTO updateSlotAvailability(@PathVariable UUID slotId,
                                                     @RequestParam boolean isAvailable) {
        return appointmentSlotService.updateAppointmentSlotAvailability(slotId, isAvailable);
    }
}
