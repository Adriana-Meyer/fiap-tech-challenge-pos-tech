package com.fiap.workshop.management.application.dto.vehicle;

import com.fiap.workshop.management.domain.model.vehicle.Vehicle;

import java.util.UUID;

public record VehicleResponse(
        UUID id,
        String plate,
        String brand,
        String model,
        int year,
        String color,
        UUID customerId
) {
    public static VehicleResponse from(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getPlate().formatted(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getColor(),
                vehicle.getCustomerId()
        );
    }
}
