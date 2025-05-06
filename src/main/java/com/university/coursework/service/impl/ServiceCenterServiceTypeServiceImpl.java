package com.university.coursework.service.impl;

import com.university.coursework.domain.ServiceCenterServiceTypeDTO;
import com.university.coursework.entity.ServiceCenterServiceTypeEntity;
import com.university.coursework.repository.ServiceCenterServiceTypeRepository;
import com.university.coursework.service.ServiceCenterServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceCenterServiceTypeServiceImpl implements ServiceCenterServiceTypeService {

    private final ServiceCenterServiceTypeRepository serviceCenterServiceRepository;

    @Override
    public ServiceCenterServiceTypeDTO addServiceToCenter(ServiceCenterServiceTypeDTO dto) {
        ServiceCenterServiceTypeEntity entity = ServiceCenterServiceTypeEntity.builder()
                .centerId(dto.getCenterId())
                .serviceId(dto.getServiceId())
                .price(dto.getPrice())
                .duration(dto.getDuration())
                .build();
        ServiceCenterServiceTypeEntity savedEntity = serviceCenterServiceRepository.save(entity);
        return mapToDTO(savedEntity);
    }

    @Override
    public ServiceCenterServiceTypeDTO getServiceMapping(UUID id) {
        ServiceCenterServiceTypeEntity entity = serviceCenterServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mapping not found with id: " + id));
        return mapToDTO(entity);
    }

    @Override
    public List<ServiceCenterServiceTypeDTO> getServicesByCenter(UUID centerId) {
        List<ServiceCenterServiceTypeEntity> entities = serviceCenterServiceRepository.findByCenterId(centerId);
        return entities.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public ServiceCenterServiceTypeDTO updateServiceMapping(ServiceCenterServiceTypeDTO dto) {
        ServiceCenterServiceTypeEntity entity = serviceCenterServiceRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Mapping not found with id: " + dto.getId()));
        entity.setPrice(dto.getPrice());
        entity.setDuration(dto.getDuration());
        ServiceCenterServiceTypeEntity updatedEntity = serviceCenterServiceRepository.save(entity);
        return mapToDTO(updatedEntity);
    }

    @Override
    public void deleteServiceMapping(UUID id) {
        if (!serviceCenterServiceRepository.existsById(id)) {
            throw new RuntimeException("Mapping not found with id: " + id);
        }
        serviceCenterServiceRepository.deleteById(id);
    }

    private ServiceCenterServiceTypeDTO mapToDTO(ServiceCenterServiceTypeEntity entity) {
        return ServiceCenterServiceTypeDTO.builder()
                .id(entity.getId())
                .centerId(entity.getCenterId())
                .serviceId(entity.getServiceId())
                .price(entity.getPrice())
                .duration(entity.getDuration())
                .build();
    }
}
