package com.fiap.workshop.management.application.port.in.vehicle;

import java.util.UUID;

public interface DeleteVehicleInputPort {
    void execute(UUID id);
}
