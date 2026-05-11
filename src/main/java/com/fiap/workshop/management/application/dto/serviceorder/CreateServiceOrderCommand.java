package com.fiap.workshop.management.application.dto.serviceorder;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateServiceOrderCommand(
        @NotNull UUID customerId,
        @NotNull UUID vehicleId,
        String customerComments
) {}
