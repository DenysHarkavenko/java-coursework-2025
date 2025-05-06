package com.university.coursework.service.impl;

import com.university.coursework.domain.ServiceTypeDTO;
import com.university.coursework.entity.ServiceTypeEntity;
import com.university.coursework.repository.ServiceTypeRepository;
import com.university.coursework.service.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceTypeServiceImpl implements ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    @Override
    public ServiceTypeDTO createServiceType(ServiceTypeDTO serviceTypeDTO) {
        ServiceTypeEntity entity = ServiceTypeEntity.builder()
                .name(serviceTypeDTO.getName())
                .description(serviceTypeDTO.getDescription())
                .category(serviceTypeDTO.getCategory())
                .build();
        ServiceTypeEntity savedEntity = serviceTypeRepository.save(entity);
        return mapToDTO(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceTypeDTO getServiceTypeById(UUID serviceId) {
        ServiceTypeEntity entity = serviceTypeRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service Type not found with id: " + serviceId));
        return mapToDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceTypeDTO> getAllServiceTypes() {
        List<ServiceTypeEntity> entities = serviceTypeRepository.findAll();
        return entities.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public ServiceTypeDTO updateServiceType(UUID serviceId, ServiceTypeDTO serviceTypeDTO) {
        ServiceTypeEntity entity = serviceTypeRepository.findById(serviceTypeDTO.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service Type not found with id: " + serviceTypeDTO.getServiceId()));
        entity.setName(serviceTypeDTO.getName());
        entity.setDescription(serviceTypeDTO.getDescription());
        entity.setCategory(serviceTypeDTO.getCategory());
        ServiceTypeEntity updatedEntity = serviceTypeRepository.save(entity);
        return mapToDTO(updatedEntity);
    }

    @Override
    public void deleteServiceType(UUID serviceId) {
        if (!serviceTypeRepository.existsById(serviceId)) {
            throw new RuntimeException("Service Type not found with id: " + serviceId);
        }
        serviceTypeRepository.deleteById(serviceId);
    }

    private ServiceTypeDTO mapToDTO(ServiceTypeEntity entity) {
        return ServiceTypeDTO.builder()
                .serviceId(entity.getServiceId())
                .name(entity.getName())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .build();
    }
}
