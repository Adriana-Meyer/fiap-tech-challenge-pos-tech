package com.fiap.workshop.management.application.port.in.supply;

import com.fiap.workshop.management.application.dto.supply.StockAdjustmentCommand;
import com.fiap.workshop.management.application.dto.supply.SupplyResponse;

import java.util.UUID;

public interface AdjustStockInputPort {
    SupplyResponse execute(UUID id, StockAdjustmentCommand command);
}
