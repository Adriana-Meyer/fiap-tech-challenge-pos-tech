package com.fiap.workshop.management.application.dto.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateVehicleCommand(
        @NotBlank String brand,
        @NotBlank String model,
        @NotNull Integer year,
        String color
) {}
