package com.fiap.workshop.management.application.port.in.supply;

import com.fiap.workshop.management.application.dto.supply.SupplyResponse;
import com.fiap.workshop.management.application.dto.supply.UpsertSupplyCommand;

public interface CreateSupplyInputPort {
    SupplyResponse execute(UpsertSupplyCommand command);
}
