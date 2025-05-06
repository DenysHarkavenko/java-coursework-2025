package com.university.coursework.controller;

import com.university.coursework.domain.AppointmentSlotDTO;
import com.university.coursework.service.AppointmentSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/slots")
@RequiredArgsConstructor
@Tag(name = "Appointment Slot Management", description = "APIs for managing appointment slots")
public class AppointmentSlotController {

    private final AppointmentSlotService appointmentSlotService;

    @GetMapping("/center/{centerId}")
    @Operation(summary = "Get available slots by center", description = "Retrieves all available appointment slots for a specific service center")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment slots retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Service center not found")
    })
    public List<AppointmentSlotDTO> getAvailableSlotsByCenter(
            @Parameter(description = "ID of the service center") @PathVariable UUID centerId) {
        return appointmentSlotService.getAvailableSlots(centerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create appointment slot", description = "Creates new appointment slot. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment slot created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public AppointmentSlotDTO createSlots(@RequestBody AppointmentSlotDTO slotDTO) {
        return appointmentSlotService.addAppointmentSlot(slotDTO);
    }

    @PutMapping("/{slotId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update appointment slot", description = "Updates an existing appointment slot. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment slot updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Appointment slot not found")
    })
    public AppointmentSlotDTO updateSlot(
            @Parameter(description = "ID of the appointment slot to update") @PathVariable UUID slotId,
            @RequestBody AppointmentSlotDTO slotDTO) {
        return appointmentSlotService.updateSlot(slotId, slotDTO);
    }

    @DeleteMapping("/{slotId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete appointment slot", description = "Deletes an appointment slot. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment slot deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Appointment slot not found")
    })
    public void deleteSlot(@Parameter(description = "ID of the appointment slot to delete") @PathVariable UUID slotId) {
        appointmentSlotService.deleteSlot(slotId);
    }
}
