package com.university.coursework.controller;

import com.university.coursework.domain.ServiceTypeDTO;
import com.university.coursework.service.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/service-types")
@RequiredArgsConstructor
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceTypeDTO createServiceType(@RequestBody ServiceTypeDTO dto) {
        return serviceTypeService.createServiceType(dto);
    }

    @GetMapping("/{serviceId}")
    public ServiceTypeDTO getServiceType(@PathVariable UUID serviceId) {
        return serviceTypeService.getServiceTypeById(serviceId);
    }

    @GetMapping
    public List<ServiceTypeDTO> getAllServiceTypes() {
        return serviceTypeService.getAllServiceTypes();
    }

    @PutMapping("/{serviceId}")
    public ServiceTypeDTO updateServiceType(@PathVariable UUID serviceId, @RequestBody ServiceTypeDTO dto) {
        return serviceTypeService.updateServiceType(dto.toBuilder().serviceId(serviceId).build());
    }

    @DeleteMapping("/{serviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteServiceType(@PathVariable UUID serviceId) {
        serviceTypeService.deleteServiceType(serviceId);
    }
}
