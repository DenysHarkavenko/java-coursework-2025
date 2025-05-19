package com.university.coursework.controller;

import com.university.coursework.domain.ServiceRequestDTO;
import com.university.coursework.repository.UserRepository;
import com.university.coursework.service.ServiceRequestService;
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
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
@Tag(name = "Service Request Management", description = "APIs for managing service requests")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{requestId}")
    @Operation(summary = "Get service request by ID", description = "Retrieves a service request by its ID. Accessible by administrators and the request owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service request found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Service request not found")
    })
    public ServiceRequestDTO getRequest(@Parameter(description = "ID of the service request to retrieve") @PathVariable UUID requestId) {
        return serviceRequestService.getServiceRequestById(requestId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new service request", description = "Creates a new service request. Accessible by authenticated users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Service request created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ServiceRequestDTO addRequest(@RequestBody ServiceRequestDTO requestDTO) {
        return serviceRequestService.addServiceRequest(requestDTO);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/users/{userId}")
    @Operation(summary = "Get service requests by user", description = "Retrieves all service requests for a specific user. Accessible by administrators and the user themselves.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service requests found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public List<ServiceRequestDTO> getRequestsByUser(@Parameter(description = "ID of the user") @PathVariable UUID userId) {
        return serviceRequestService.getServiceRequestsByUser(userId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{requestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete service request", description = "Deletes a service request. Accessible by administrators and the request owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Service request deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Service request not found")
    })
    public void deleteRequest(@Parameter(description = "ID of the service request to delete") @PathVariable UUID requestId) {
        serviceRequestService.deleteServiceRequest(requestId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{requestId}")
    @Operation(summary = "Update service request", description = "Updates an existing service request. Accessible by administrators and the request owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service request updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Service request not found")
    })
    public ServiceRequestDTO updateRequest(
            @Parameter(description = "ID of the service request to update") @PathVariable UUID requestId,
            @RequestBody ServiceRequestDTO requestDTO) {
        ServiceRequestDTO updatedRequest = requestDTO.toBuilder()
                .requestId(requestId)
                .build();
        return serviceRequestService.updateServiceRequest(updatedRequest);
    }

}
