package com.fiap.workshop.management.infrastructure.persistence.repository;

import com.fiap.workshop.management.infrastructure.persistence.entity.VehicleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleJpaRepository extends JpaRepository<VehicleJpaEntity, String> {

    Optional<VehicleJpaEntity> findByPlateValue(String plateValue);

    List<VehicleJpaEntity> findByCustomerId(String customerId);

    boolean existsByPlateValue(String plateValue);
}
