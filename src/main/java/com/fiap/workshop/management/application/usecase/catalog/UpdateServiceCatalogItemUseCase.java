package com.fiap.workshop.management.application.usecase.catalog;

import com.fiap.workshop.management.application.dto.catalog.ServiceCatalogItemResponse;
import com.fiap.workshop.management.application.dto.catalog.UpsertServiceCatalogItemCommand;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.repository.ServiceCatalogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UpdateServiceCatalogItemUseCase {

    private final ServiceCatalogRepository serviceCatalogRepository;

    public UpdateServiceCatalogItemUseCase(ServiceCatalogRepository serviceCatalogRepository) {
        this.serviceCatalogRepository = serviceCatalogRepository;
    }

    @Transactional
    public ServiceCatalogItemResponse execute(UUID id, UpsertServiceCatalogItemCommand command) {
        ServiceCatalogItem item = serviceCatalogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceCatalogItem", id));
        item.update(command.name(), command.description(), command.type(), Money.of(command.basePrice()));
        if (Boolean.TRUE.equals(command.active())) {
            item.activate();
        } else if (Boolean.FALSE.equals(command.active())) {
            item.deactivate();
        }
        return ServiceCatalogItemResponse.from(serviceCatalogRepository.save(item));
    }
}
