package com.university.coursework.service;

import com.university.coursework.domain.ServiceRequestDTO;
import java.util.List;
import java.util.UUID;

public interface ServiceRequestService {
    ServiceRequestDTO addServiceRequest(ServiceRequestDTO requestDTO);
    ServiceRequestDTO getServiceRequestById(UUID requestId);
    List<ServiceRequestDTO> getServiceRequestsByUser(UUID userId);
    ServiceRequestDTO updateServiceRequest(ServiceRequestDTO requestDTO);
    void deleteServiceRequest(UUID requestId);
}
