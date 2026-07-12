package com.fiap.workshop.management.application.usecase.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderSummaryResponse;
import com.fiap.workshop.management.application.port.in.serviceorder.FindServiceOrderInputPort;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderStatus;
import com.fiap.workshop.management.domain.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class FindServiceOrderUseCase implements FindServiceOrderInputPort {

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
    public List<ServiceOrderSummaryResponse> findAll(boolean includeCompleted) {
        return serviceOrderRepository.findAll().stream()
                .filter(order -> includeCompleted || isActive(order.getStatus()))
                .sorted(Comparator.comparing(ServiceOrder::getStatus)
                        .reversed()
                        .thenComparing(ServiceOrder::getReceivedAt))
                .map(ServiceOrderSummaryResponse::from)
                .toList();
    }

    private boolean isActive(ServiceOrderStatus status) {
        return status != ServiceOrderStatus.FINISHED && status != ServiceOrderStatus.DELIVERED;
    }
}
