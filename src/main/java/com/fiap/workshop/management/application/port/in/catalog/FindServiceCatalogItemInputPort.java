package com.fiap.workshop.management.application.port.in.catalog;

import com.fiap.workshop.management.application.dto.catalog.ServiceCatalogItemResponse;

import java.util.List;
import java.util.UUID;

public interface FindServiceCatalogItemInputPort {
    ServiceCatalogItemResponse findById(UUID id);
    List<ServiceCatalogItemResponse> findAll();
    List<ServiceCatalogItemResponse> findAllActive();
}
