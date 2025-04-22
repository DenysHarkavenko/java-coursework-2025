package com.university.coursework.controller;

import com.university.coursework.domain.ServiceRequestDTO;
import com.university.coursework.service.ServiceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceRequestDTO addRequest(@RequestBody ServiceRequestDTO requestDTO) {
        return serviceRequestService.addServiceRequest(requestDTO);
    }

    @GetMapping("/{requestId}")
    public ServiceRequestDTO getRequest(@PathVariable UUID requestId) {
        return serviceRequestService.getServiceRequestById(requestId);
    }

    @GetMapping("/users/{userId}")
    public List<ServiceRequestDTO> getRequestsByUser(@PathVariable UUID userId) {
        return serviceRequestService.getServiceRequestsByUser(userId);
    }

    @PutMapping("/{requestId}")
    public ServiceRequestDTO updateRequest(@PathVariable UUID requestId,
                                           @RequestBody ServiceRequestDTO requestDTO) {
        ServiceRequestDTO updatedRequest = requestDTO.toBuilder()
                .requestId(requestId)
                .build();
        return serviceRequestService.updateServiceRequest(updatedRequest);
    }

    @DeleteMapping("/{requestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequest(@PathVariable UUID requestId) {
        serviceRequestService.deleteServiceRequest(requestId);
    }
}
