package com.fiap.workshop.management.application.dto.serviceorder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleIntakeCommand(
        @NotBlank String plate,
        @NotBlank String brand,
        @NotBlank String model,
        @NotNull Integer year,
        String color
) {}
