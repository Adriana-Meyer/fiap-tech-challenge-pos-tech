package com.fiap.workshop.management.infrastructure.persistence.adapter;

import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.repository.ServiceCatalogRepository;
import com.fiap.workshop.management.infrastructure.persistence.mapper.ServiceCatalogItemMapper;
import com.fiap.workshop.management.infrastructure.persistence.repository.ServiceCatalogJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ServiceCatalogRepositoryAdapter implements ServiceCatalogRepository {

    private final ServiceCatalogJpaRepository jpaRepository;
    private final ServiceCatalogItemMapper mapper;

    public ServiceCatalogRepositoryAdapter(ServiceCatalogJpaRepository jpaRepository, ServiceCatalogItemMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public ServiceCatalogItem save(ServiceCatalogItem item) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(item)));
    }

    @Override
    public Optional<ServiceCatalogItem> findById(UUID id) {
        return jpaRepository.findById(id.toString()).map(mapper::toDomain);
    }

    @Override
    public List<ServiceCatalogItem> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<ServiceCatalogItem> findAllActive() {
        return jpaRepository.findByActiveTrue().stream().map(mapper::toDomain).toList();
    }
}
