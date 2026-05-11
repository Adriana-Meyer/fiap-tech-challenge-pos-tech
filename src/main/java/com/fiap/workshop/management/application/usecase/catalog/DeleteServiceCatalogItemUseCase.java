package com.fiap.workshop.management.application.usecase.catalog;

import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.repository.ServiceCatalogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteServiceCatalogItemUseCase {

    private final ServiceCatalogRepository serviceCatalogRepository;

    public DeleteServiceCatalogItemUseCase(ServiceCatalogRepository serviceCatalogRepository) {
        this.serviceCatalogRepository = serviceCatalogRepository;
    }

    @Transactional
    public void execute(UUID id) {
        ServiceCatalogItem item = serviceCatalogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceCatalogItem", id));
        item.deactivate();
        serviceCatalogRepository.save(item);
    }
}
