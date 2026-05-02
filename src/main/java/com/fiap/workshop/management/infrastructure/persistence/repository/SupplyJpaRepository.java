package com.fiap.workshop.management.infrastructure.persistence.repository;

import com.fiap.workshop.management.infrastructure.persistence.entity.SupplyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SupplyJpaRepository extends JpaRepository<SupplyJpaEntity, String> {

    Optional<SupplyJpaEntity> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT s FROM SupplyJpaEntity s WHERE s.stockQuantity < s.minimumStock")
    List<SupplyJpaEntity> findBelowMinimumStock();
}
