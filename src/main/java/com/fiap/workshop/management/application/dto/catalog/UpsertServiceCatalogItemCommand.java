package com.fiap.workshop.management.application.dto.catalog;

import com.fiap.workshop.management.domain.model.catalog.ServiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpsertServiceCatalogItemCommand(
        @NotBlank String name,
        String description,
        @NotNull ServiceType type,
        @NotNull @Positive BigDecimal basePrice
) {}
