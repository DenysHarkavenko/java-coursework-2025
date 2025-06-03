package com.university.coursework.controller;

import com.university.coursework.domain.ServiceCenterDTO;
import com.university.coursework.service.CenterService;
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
@RequestMapping("/api/v1/centers")
@RequiredArgsConstructor
@Tag(name = "Service Center Management", description = "APIs for managing service centers")
public class ServiceCenterController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-rated")
    @Operation(summary = "Get top-rated service centers", description = "Retrieves a list of top-rated service centers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top-rated service centers retrieved successfully")
    })
    public List<ServiceCenterDTO> getTopRatedCenters() {
        return centerService.getTopRatedCenters();
    }

    private final CenterService centerService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all service centers", description = "Retrieves all service centers. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service centers retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public List<ServiceCenterDTO> getAllCenters() {
        return centerService.getAllCenters();
    }

    @GetMapping("/{centerId}")
    @Operation(summary = "Get service center by ID", description = "Retrieves a service center by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service center found"),
            @ApiResponse(responseCode = "404", description = "Service center not found")
    })
    public ServiceCenterDTO getCenter(@Parameter(description = "ID of the service center") @PathVariable UUID centerId) {
        return centerService.getCenterById(centerId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new service center", description = "Creates a new service center. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Service center created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ServiceCenterDTO createCenter(@RequestBody ServiceCenterDTO centerDTO) {
        return centerService.addNewCenter(centerDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{centerId}")
    @Operation(summary = "Update service center", description = "Updates an existing service center. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service center updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Service center not found")
    })
    public ServiceCenterDTO updateCenter(
            @Parameter(description = "ID of the service center to update") @PathVariable UUID centerId,
            @RequestBody ServiceCenterDTO centerDTO) {
        return centerService.updateCenter(centerId, centerDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{centerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete service center", description = "Deletes a service center. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Service center deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Service center not found")
    })
    public void deleteCenter(@Parameter(description = "ID of the service center to delete") @PathVariable UUID centerId) {
        centerService.deleteCenter(centerId);
    }
}
