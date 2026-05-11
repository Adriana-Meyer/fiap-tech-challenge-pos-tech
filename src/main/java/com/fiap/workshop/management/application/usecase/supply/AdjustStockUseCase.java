package com.fiap.workshop.management.application.usecase.supply;

import com.fiap.workshop.management.application.dto.supply.StockAdjustmentCommand;
import com.fiap.workshop.management.application.dto.supply.SupplyResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.supply.Supply;
import com.fiap.workshop.management.domain.repository.SupplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AdjustStockUseCase {

    private final SupplyRepository supplyRepository;

    public AdjustStockUseCase(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    @Transactional
    public SupplyResponse execute(UUID id, StockAdjustmentCommand command) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supply", id));
        supply.addStock(command.quantity());
        return SupplyResponse.from(supplyRepository.save(supply));
    }
}
