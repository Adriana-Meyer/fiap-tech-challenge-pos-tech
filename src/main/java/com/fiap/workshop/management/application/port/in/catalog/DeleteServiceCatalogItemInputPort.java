package com.fiap.workshop.management.application.port.in.catalog;

import java.util.UUID;

public interface DeleteServiceCatalogItemInputPort {
    void execute(UUID id);
}
