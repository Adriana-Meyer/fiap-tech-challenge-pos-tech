package com.fiap.workshop.management.application.usecase.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderSummaryResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FindServiceOrderUseCase {

    private final ServiceOrderRepository serviceOrderRepository;

    public FindServiceOrderUseCase(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Transactional(readOnly = true)
    public ServiceOrderResponse findById(UUID id) {
        return serviceOrderRepository.findById(id)
                .map(ServiceOrderResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder", id));
    }

    @Transactional(readOnly = true)
    public List<ServiceOrderSummaryResponse> findAll() {
        return serviceOrderRepository.findAll().stream()
                .map(ServiceOrderSummaryResponse::from)
                .toList();
    }
}
