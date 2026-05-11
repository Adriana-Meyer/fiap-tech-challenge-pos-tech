package com.fiap.workshop.management.infrastructure.persistence.adapter;

import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.catalog.ServiceType;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.repository.ServiceCatalogRepository;
import com.fiap.workshop.management.infrastructure.persistence.entity.ServiceCatalogItemJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.repository.ServiceCatalogJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ServiceCatalogRepositoryAdapter implements ServiceCatalogRepository {

    private final ServiceCatalogJpaRepository jpaRepository;

    public ServiceCatalogRepositoryAdapter(ServiceCatalogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ServiceCatalogItem save(ServiceCatalogItem item) {
        return toDomain(jpaRepository.save(toEntity(item)));
    }

    @Override
    public Optional<ServiceCatalogItem> findById(UUID id) {
        return jpaRepository.findById(id.toString()).map(this::toDomain);
    }

    @Override
    public List<ServiceCatalogItem> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<ServiceCatalogItem> findAllActive() {
        return jpaRepository.findByActiveTrue().stream().map(this::toDomain).toList();
    }

    private ServiceCatalogItem toDomain(ServiceCatalogItemJpaEntity e) {
        return new ServiceCatalogItem(
                UUID.fromString(e.getId()),
                e.getName(),
                e.getDescription(),
                ServiceType.valueOf(e.getType()),
                Money.of(e.getBasePrice()),
                e.isActive()
        );
    }

    private ServiceCatalogItemJpaEntity toEntity(ServiceCatalogItem item) {
        return new ServiceCatalogItemJpaEntity(
                item.getId().toString(),
                item.getName(),
                item.getDescription(),
                item.getType().name(),
                item.getBasePrice().getAmount(),
                item.isActive()
        );
    }
}
