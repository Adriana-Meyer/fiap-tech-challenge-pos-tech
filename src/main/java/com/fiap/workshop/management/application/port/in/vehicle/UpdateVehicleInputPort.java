package com.fiap.workshop.management.application.port.in.vehicle;

import com.fiap.workshop.management.application.dto.vehicle.UpdateVehicleCommand;
import com.fiap.workshop.management.application.dto.vehicle.VehicleResponse;

import java.util.UUID;

public interface UpdateVehicleInputPort {
    VehicleResponse execute(UUID id, UpdateVehicleCommand command);
}
