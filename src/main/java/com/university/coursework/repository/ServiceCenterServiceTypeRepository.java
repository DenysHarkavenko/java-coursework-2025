package com.university.coursework.repository;

import com.university.coursework.entity.ServiceCenterServiceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceCenterServiceTypeRepository extends JpaRepository<ServiceCenterServiceTypeEntity, UUID> {
    List<ServiceCenterServiceTypeEntity> findByCenterId(UUID centerId);
}
