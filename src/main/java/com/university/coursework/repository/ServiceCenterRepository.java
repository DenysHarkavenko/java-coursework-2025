package com.university.coursework.repository;

import com.university.coursework.entity.ServiceCenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceCenterRepository extends JpaRepository<ServiceCenterEntity, UUID> {
    List<ServiceCenterEntity> findAllByOrderByRatingDesc();
    List<ServiceCenterEntity> findByRegion(String region);
}
