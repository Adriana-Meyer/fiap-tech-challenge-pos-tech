package com.fiap.workshop.management.application.usecase.catalog;

import com.fiap.workshop.management.application.dto.catalog.ServiceCatalogItemResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.repository.ServiceCatalogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FindServiceCatalogItemUseCase {

    private final ServiceCatalogRepository serviceCatalogRepository;

    public FindServiceCatalogItemUseCase(ServiceCatalogRepository serviceCatalogRepository) {
        this.serviceCatalogRepository = serviceCatalogRepository;
    }

    @Transactional(readOnly = true)
    public ServiceCatalogItemResponse findById(UUID id) {
        return serviceCatalogRepository.findById(id)
                .map(ServiceCatalogItemResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceCatalogItem", id));
    }

    @Transactional(readOnly = true)
    public List<ServiceCatalogItemResponse> findAll() {
        return serviceCatalogRepository.findAll().stream()
                .map(ServiceCatalogItemResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ServiceCatalogItemResponse> findAllActive() {
        return serviceCatalogRepository.findAllActive().stream()
                .map(ServiceCatalogItemResponse::from)
                .toList();
    }
}
