package com.university.coursework.service;

import com.university.coursework.domain.ServiceTypeDTO;
import java.util.List;
import java.util.UUID;

public interface ServiceTypeService {
    ServiceTypeDTO createServiceType(ServiceTypeDTO serviceTypeDTO);
    ServiceTypeDTO getServiceTypeById(UUID serviceId);
    List<ServiceTypeDTO> getAllServiceTypes();
    ServiceTypeDTO updateServiceType(UUID serviceId, ServiceTypeDTO serviceTypeDTO);
    void deleteServiceType(UUID serviceId);
}
