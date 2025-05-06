package com.university.coursework.repository;

import com.university.coursework.entity.ServiceRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequestEntity, UUID> {
    List<ServiceRequestEntity> findByUserId(UUID userId);
    List<ServiceRequestEntity> findByCenterId(UUID centerId);
}
