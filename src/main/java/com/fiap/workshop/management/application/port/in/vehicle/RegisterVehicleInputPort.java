package com.fiap.workshop.management.application.port.in.vehicle;

import com.fiap.workshop.management.application.dto.vehicle.RegisterVehicleCommand;
import com.fiap.workshop.management.application.dto.vehicle.VehicleResponse;

public interface RegisterVehicleInputPort {
    VehicleResponse execute(RegisterVehicleCommand command);
}
