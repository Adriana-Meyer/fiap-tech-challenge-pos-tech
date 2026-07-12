package com.fiap.workshop.management.application.port.in.supply;

import com.fiap.workshop.management.application.dto.supply.SupplyResponse;

import java.util.List;
import java.util.UUID;

public interface FindSupplyInputPort {
    SupplyResponse findById(UUID id);
    List<SupplyResponse> findAll();
    List<SupplyResponse> findBelowMinimumStock();
}
