package com.fiap.workshop.management.application.port.in.supply;

import com.fiap.workshop.management.application.dto.supply.SupplyResponse;
import com.fiap.workshop.management.application.dto.supply.UpsertSupplyCommand;

import java.util.UUID;

public interface UpdateSupplyInputPort {
    SupplyResponse execute(UUID id, UpsertSupplyCommand command);
}
