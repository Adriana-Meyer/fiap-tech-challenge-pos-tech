package com.fiap.workshop.management.infrastructure.persistence.repository;

import com.fiap.workshop.management.infrastructure.persistence.entity.ServiceOrderItemJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.entity.ServiceOrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceOrderJpaRepository extends JpaRepository<ServiceOrderJpaEntity, String> {

    Optional<ServiceOrderJpaEntity> findByOsCode(String osCode);

    @Query("SELECT COUNT(o) FROM ServiceOrderJpaEntity o WHERE YEAR(o.receivedAt) = :year")
    long countByYear(@Param("year") int year);

    @Query("SELECT i FROM ServiceOrderItemJpaEntity i " +
           "WHERE i.executionStartedAt IS NOT NULL " +
           "AND i.executionFinishedAt IS NOT NULL " +
           "AND i.serviceCatalogItem IS NOT NULL")
    List<ServiceOrderItemJpaEntity> findCompletedServiceItems();
}
