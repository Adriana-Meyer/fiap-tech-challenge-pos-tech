package com.fiap.workshop.management.application.port.in.vehicle;

import com.fiap.workshop.management.application.dto.vehicle.VehicleResponse;

import java.util.List;
import java.util.UUID;

public interface FindVehicleInputPort {
    VehicleResponse findById(UUID id);
    VehicleResponse findByPlate(String plate);
    List<VehicleResponse> findByCustomerId(UUID customerId);
    List<VehicleResponse> findAll();
}
