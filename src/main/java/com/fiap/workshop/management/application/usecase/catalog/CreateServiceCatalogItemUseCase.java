package com.fiap.workshop.management.application.usecase.catalog;

import com.fiap.workshop.management.application.dto.catalog.ServiceCatalogItemResponse;
import com.fiap.workshop.management.application.dto.catalog.UpsertServiceCatalogItemCommand;
import com.fiap.workshop.management.application.port.in.catalog.CreateServiceCatalogItemInputPort;
import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.repository.ServiceCatalogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateServiceCatalogItemUseCase implements CreateServiceCatalogItemInputPort {

    private final ServiceCatalogRepository serviceCatalogRepository;

    public CreateServiceCatalogItemUseCase(ServiceCatalogRepository serviceCatalogRepository) {
        this.serviceCatalogRepository = serviceCatalogRepository;
    }

    @Transactional
    public ServiceCatalogItemResponse execute(UpsertServiceCatalogItemCommand command) {
        ServiceCatalogItem item = ServiceCatalogItem.create(
                command.name(),
                command.description(),
                command.type(),
                Money.of(command.basePrice())
        );
        return ServiceCatalogItemResponse.from(serviceCatalogRepository.save(item));
    }
}
