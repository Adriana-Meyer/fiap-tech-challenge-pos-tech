package com.fiap.workshop.management.application.dto.catalog;

import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;

import java.math.BigDecimal;
import java.util.UUID;

public record ServiceCatalogItemResponse(
        UUID id,
        String name,
        String description,
        String type,
        BigDecimal basePrice,
        boolean active
) {
    public static ServiceCatalogItemResponse from(ServiceCatalogItem item) {
        return new ServiceCatalogItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getType().name(),
                item.getBasePrice().getAmount(),
                item.isActive()
        );
    }
}
