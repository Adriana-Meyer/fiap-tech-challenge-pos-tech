package com.fiap.workshop.management.application.usecase.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;
import com.fiap.workshop.management.domain.repository.ServiceOrderRepository;
import com.fiap.workshop.management.domain.service.StockManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ApproveEstimateUseCase {

    private final ServiceOrderRepository serviceOrderRepository;
    private final StockManagementService stockManagementService;

    public ApproveEstimateUseCase(ServiceOrderRepository serviceOrderRepository,
                                   StockManagementService stockManagementService) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.stockManagementService = stockManagementService;
    }

    @Transactional
    public ServiceOrderResponse execute(UUID id) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder", id));
        order.approveEstimate();
        stockManagementService.deductStock(order.getItems());
        return ServiceOrderResponse.from(serviceOrderRepository.save(order));
    }
}
