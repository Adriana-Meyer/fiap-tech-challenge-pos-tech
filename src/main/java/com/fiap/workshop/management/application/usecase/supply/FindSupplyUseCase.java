package com.fiap.workshop.management.application.usecase.supply;

import com.fiap.workshop.management.application.dto.supply.SupplyResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.repository.SupplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FindSupplyUseCase {

    private final SupplyRepository supplyRepository;

    public FindSupplyUseCase(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    @Transactional(readOnly = true)
    public SupplyResponse findById(UUID id) {
        return supplyRepository.findById(id)
                .map(SupplyResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Supply", id));
    }

    @Transactional(readOnly = true)
    public List<SupplyResponse> findAll() {
        return supplyRepository.findAll().stream().map(SupplyResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<SupplyResponse> findBelowMinimumStock() {
        return supplyRepository.findBelowMinimumStock().stream().map(SupplyResponse::from).toList();
    }
}
