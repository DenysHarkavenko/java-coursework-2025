package com.university.coursework.repository;

import com.university.coursework.entity.AppointmentSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlotEntity, UUID> {
    List<AppointmentSlotEntity> findByCenterIdAndIsAvailableTrue(UUID centerId);
}
