package com.fiap.workshop.management.domain.service;

import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderItem;
import com.fiap.workshop.management.domain.model.supply.Supply;
import com.fiap.workshop.management.domain.repository.SupplyRepository;

import java.util.List;

public class StockManagementService {

    private final SupplyRepository supplyRepository;

    public StockManagementService(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    public List<Supply> deductStock(List<ServiceOrderItem> items) {
        List<Supply> deducted = new java.util.ArrayList<>();
        for (ServiceOrderItem item : items) {
            if (item.getSupply() == null) continue;
            Supply supply = supplyRepository.findById(item.getSupply().getId())
                    .orElseThrow(() -> new IllegalStateException("Supply not found: " + item.getSupply().getId()));
            supply.deductStock(item.getQuantity());
            supplyRepository.save(supply);
            deducted.add(supply);
        }
        return deducted;
    }
}
