package com.university.coursework.service;

import com.university.coursework.domain.ServiceCenterServiceTypeDTO;

import java.util.List;
import java.util.UUID;

public interface ServiceCenterServiceTypeService {
    ServiceCenterServiceTypeDTO addServiceToCenter(ServiceCenterServiceTypeDTO dto);
    ServiceCenterServiceTypeDTO getServiceMapping(UUID id);
    List<ServiceCenterServiceTypeDTO> getServicesByCenter(UUID centerId);
    ServiceCenterServiceTypeDTO updateServiceMapping(ServiceCenterServiceTypeDTO dto);
    void deleteServiceMapping(UUID id);
}
