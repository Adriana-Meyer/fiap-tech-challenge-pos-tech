package com.fiap.workshop.management.domain.repository;

import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceCatalogRepository {

    ServiceCatalogItem save(ServiceCatalogItem item);

    Optional<ServiceCatalogItem> findById(UUID id);

    List<ServiceCatalogItem> findAll();

    List<ServiceCatalogItem> findAllActive();
}
