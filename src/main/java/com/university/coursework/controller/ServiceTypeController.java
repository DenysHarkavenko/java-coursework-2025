package com.university.coursework.controller;

import com.university.coursework.domain.ServiceTypeDTO;
import com.university.coursework.service.ServiceTypeService;
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
@RequestMapping("/api/v1/service-types")
@RequiredArgsConstructor
@Tag(name = "Service Type Management", description = "APIs for managing service types")
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    @GetMapping
    @Operation(summary = "Get all service types", description = "Retrieves all available service types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service types retrieved successfully")
    })
    public List<ServiceTypeDTO> getAllServiceTypes() {
        return serviceTypeService.getAllServiceTypes();
    }

    @GetMapping("/{serviceId}")
    @Operation(summary = "Get service type by ID", description = "Retrieves a service type by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service type found"),
            @ApiResponse(responseCode = "404", description = "Service type not found")
    })
    public ServiceTypeDTO getServiceType(@Parameter(description = "ID of the service type") @PathVariable UUID serviceId) {
        return serviceTypeService.getServiceTypeById(serviceId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new service type", description = "Creates a new service type. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Service type created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ServiceTypeDTO createServiceType(@RequestBody ServiceTypeDTO serviceTypeDTO) {
        return serviceTypeService.createServiceType(serviceTypeDTO);
    }

    @PutMapping("/{serviceId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update service type", description = "Updates an existing service type. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service type updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Service type not found")
    })
    public ServiceTypeDTO updateServiceType(
            @Parameter(description = "ID of the service type to update") @PathVariable UUID serviceId,
            @RequestBody ServiceTypeDTO serviceTypeDTO) {
        return serviceTypeService.updateServiceType(serviceId, serviceTypeDTO);
    }

    @DeleteMapping("/{serviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete service type", description = "Deletes a service type. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Service type deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Service type not found")
    })
    public void deleteServiceType(@Parameter(description = "ID of the service type to delete") @PathVariable UUID serviceId) {
        serviceTypeService.deleteServiceType(serviceId);
    }
}
