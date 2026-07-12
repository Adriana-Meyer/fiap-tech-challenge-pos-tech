package com.fiap.workshop.management.application.port.in.supply;

import java.util.UUID;

public interface DeleteSupplyInputPort {
    void execute(UUID id);
}
