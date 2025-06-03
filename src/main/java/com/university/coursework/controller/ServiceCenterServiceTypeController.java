package com.university.coursework.controller;

import com.university.coursework.domain.ServiceCenterServiceTypeDTO;
import com.university.coursework.service.ServiceCenterServiceTypeService;
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
@RequestMapping("/api/v1/center-services")
@RequiredArgsConstructor
@Tag(name = "Service Center Service Type Management", description = "APIs for managing service types offered by service centers")
public class ServiceCenterServiceTypeController {

    private final ServiceCenterServiceTypeService serviceCenterServiceTypeService;

    @GetMapping("/center/{centerId}")
    @Operation(summary = "Get services by center", description = "Retrieves all services offered by a specific service center")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Service center not found")
    })
    public List<ServiceCenterServiceTypeDTO> getServicesByCenter(
            @Parameter(description = "ID of the service center") @PathVariable UUID centerId) {
        return serviceCenterServiceTypeService.getServicesByCenter(centerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add service to center", description = "Adds a new service to a service center. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Service added successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ServiceCenterServiceTypeDTO addServiceToCenter(@RequestBody ServiceCenterServiceTypeDTO serviceDTO) {
        return serviceCenterServiceTypeService.addServiceToCenter(serviceDTO);
    }

    @DeleteMapping("/{serviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove service from center", description = "Removes a service from a service center. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Service removed successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    public void removeServiceFromCenter(@Parameter(description = "ID of the service to remove") @PathVariable UUID serviceId) {
        serviceCenterServiceTypeService.deleteServiceMapping(serviceId);
    }
}
