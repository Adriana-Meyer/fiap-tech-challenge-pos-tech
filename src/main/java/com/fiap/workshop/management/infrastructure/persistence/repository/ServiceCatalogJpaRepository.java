package com.fiap.workshop.management.infrastructure.persistence.repository;

import com.fiap.workshop.management.infrastructure.persistence.entity.ServiceCatalogItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceCatalogJpaRepository extends JpaRepository<ServiceCatalogItemJpaEntity, String> {

    List<ServiceCatalogItemJpaEntity> findByActiveTrue();
}
