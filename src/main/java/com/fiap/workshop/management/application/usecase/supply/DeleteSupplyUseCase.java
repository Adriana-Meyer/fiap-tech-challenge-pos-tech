package com.fiap.workshop.management.application.usecase.supply;

import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.repository.SupplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteSupplyUseCase {

    private final SupplyRepository supplyRepository;

    public DeleteSupplyUseCase(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    @Transactional
    public void execute(UUID id) {
        if (!supplyRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Supply", id);
        }
        supplyRepository.deleteById(id);
    }
}
