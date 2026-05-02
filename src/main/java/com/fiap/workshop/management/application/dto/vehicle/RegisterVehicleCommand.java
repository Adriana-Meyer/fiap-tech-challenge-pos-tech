package com.fiap.workshop.management.application.dto.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegisterVehicleCommand(
        @NotBlank String plate,
        @NotBlank String brand,
        @NotBlank String model,
        @NotNull Integer year,
        String color,
        @NotNull UUID customerId
) {}
