package com.fiap.workshop.management.application.dto.supply;

import jakarta.validation.constraints.Positive;

public record StockAdjustmentCommand(
        @Positive int quantity
) {}
