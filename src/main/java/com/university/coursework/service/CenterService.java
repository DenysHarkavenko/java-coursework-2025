package com.university.coursework.service;

import com.university.coursework.domain.ServiceCenterDTO;
import java.util.List;
import java.util.UUID;

public interface CenterService {
    List<ServiceCenterDTO> getTopRatedCenters();
    ServiceCenterDTO getCenterDetails(UUID centerId);
    List<ServiceCenterDTO> getCentersByRegion(String region);
    ServiceCenterDTO addNewCenter(ServiceCenterDTO data);
    ServiceCenterDTO updateCenter(ServiceCenterDTO centerDTO);
    ServiceCenterDTO updateCenterRating(UUID centerId, double rating);
    void deleteCenter(UUID centerId);
}
