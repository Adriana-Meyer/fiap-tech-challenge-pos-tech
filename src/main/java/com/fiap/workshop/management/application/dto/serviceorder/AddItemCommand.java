package com.fiap.workshop.management.application.dto.serviceorder;

import jakarta.validation.constraints.Min;

import java.util.UUID;

public record AddItemCommand(
        UUID serviceCatalogItemId,
        UUID supplyId,
        @Min(1) int quantity
) {}
