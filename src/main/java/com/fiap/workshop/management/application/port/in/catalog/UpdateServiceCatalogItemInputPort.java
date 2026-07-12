package com.fiap.workshop.management.application.port.in.catalog;

import com.fiap.workshop.management.application.dto.catalog.ServiceCatalogItemResponse;
import com.fiap.workshop.management.application.dto.catalog.UpsertServiceCatalogItemCommand;

import java.util.UUID;

public interface UpdateServiceCatalogItemInputPort {
    ServiceCatalogItemResponse execute(UUID id, UpsertServiceCatalogItemCommand command);
}
