package com.fiap.workshop.management.application.usecase.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;
import com.fiap.workshop.management.domain.model.supply.Supply;
import com.fiap.workshop.management.domain.repository.ServiceOrderRepository;
import com.fiap.workshop.management.domain.service.StockAlertNotificationService;
import com.fiap.workshop.management.domain.service.StockManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ApproveEstimateUseCase {

    private final ServiceOrderRepository serviceOrderRepository;
    private final StockManagementService stockManagementService;
    private final StockAlertNotificationService stockAlertNotificationService;

    public ApproveEstimateUseCase(ServiceOrderRepository serviceOrderRepository,
                                   StockManagementService stockManagementService,
                                   StockAlertNotificationService stockAlertNotificationService) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.stockManagementService = stockManagementService;
        this.stockAlertNotificationService = stockAlertNotificationService;
    }

    @Transactional
    public ServiceOrderResponse execute(UUID id) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder", id));
        order.approveEstimate();
        List<Supply> deducted = stockManagementService.deductStock(order.getItems());
        deducted.stream()
                .filter(s -> s.getStockQuantity() < 0)
                .forEach(s -> stockAlertNotificationService.notifyNegativeStock(
                        s.getCode(), s.getName(), s.getStockQuantity()));
        return ServiceOrderResponse.from(serviceOrderRepository.save(order));
    }
}
