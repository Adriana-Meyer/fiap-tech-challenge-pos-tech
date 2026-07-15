package com.fiap.workshop.management.application.port.in.catalog;

import com.fiap.workshop.management.application.dto.catalog.ServiceCatalogItemResponse;
import com.fiap.workshop.management.application.dto.catalog.UpsertServiceCatalogItemCommand;

public interface CreateServiceCatalogItemInputPort {
    ServiceCatalogItemResponse execute(UpsertServiceCatalogItemCommand command);
}
