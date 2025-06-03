package com.university.coursework.controller;

import com.university.coursework.domain.ServiceCenterServiceTypeDTO;
import com.university.coursework.service.ServiceCenterServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/center-services")
@RequiredArgsConstructor
public class ServiceCenterServiceTypeController {

    private final ServiceCenterServiceTypeService serviceCenterServiceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceCenterServiceTypeDTO addServiceToCenter(@RequestBody ServiceCenterServiceTypeDTO dto) {
        return serviceCenterServiceService.addServiceToCenter(dto);
    }

    @GetMapping("/{id}")
    public ServiceCenterServiceTypeDTO getServiceMapping(@PathVariable UUID id) {
        return serviceCenterServiceService.getServiceMapping(id);
    }

    @GetMapping("/center/{centerId}")
    public List<ServiceCenterServiceTypeDTO> getServicesByCenter(@PathVariable UUID centerId) {
        return serviceCenterServiceService.getServicesByCenter(centerId);
    }

    @PutMapping("/{id}")
    public ServiceCenterServiceTypeDTO updateServiceMapping(@PathVariable UUID id, @RequestBody ServiceCenterServiceTypeDTO dto) {
        return serviceCenterServiceService.updateServiceMapping(dto.toBuilder().id(id).build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteServiceMapping(@PathVariable UUID id) {
        serviceCenterServiceService.deleteServiceMapping(id);
    }
}
