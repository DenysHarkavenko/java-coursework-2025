package com.university.coursework.controller;

import com.university.coursework.domain.ServiceCenterDTO;
import com.university.coursework.service.CenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/centers")
@RequiredArgsConstructor
public class ServiceCenterController {

    private final CenterService centerService;

    @GetMapping("/top-rated")
    public List<ServiceCenterDTO> getTopRatedCenters() {
        return centerService.getTopRatedCenters();
    }

    @GetMapping("/{centerId}")
    public ServiceCenterDTO getCenterDetails(@PathVariable UUID centerId) {
        return centerService.getCenterDetails(centerId);
    }

    @GetMapping
    public List<ServiceCenterDTO> getCentersByRegion(@RequestParam String region) {
        return centerService.getCentersByRegion(region);
    }

    @PostMapping
    public ServiceCenterDTO addNewCenter(@RequestBody ServiceCenterDTO centerDTO) {
        return centerService.addNewCenter(centerDTO);
    }

    @PutMapping("/{centerId}")
    public ServiceCenterDTO updateCenter(@PathVariable UUID centerId,
                                         @RequestBody ServiceCenterDTO centerDTO) {
        return centerService.updateCenter(
                centerDTO.toBuilder()
                        .centerId(centerId)
                        .build()
        );
    }

    @DeleteMapping("/{centerId}")
    public void deleteCenter(@PathVariable UUID centerId) {
        centerService.deleteCenter(centerId);
    }
}
