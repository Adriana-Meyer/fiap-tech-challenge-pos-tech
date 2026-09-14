package com.fiap.workshop.management.application.usecase.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.application.port.in.serviceorder.FinishServiceItemExecutionInputPort;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderItem;
import com.fiap.workshop.management.domain.repository.ServiceOrderRepository;
import com.fiap.workshop.management.domain.service.MetricsPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Service
public class FinishServiceItemExecutionUseCase implements FinishServiceItemExecutionInputPort {

    private final ServiceOrderRepository serviceOrderRepository;
    private final MetricsPublisher metricsPublisher;

    public FinishServiceItemExecutionUseCase(ServiceOrderRepository serviceOrderRepository,
                                              MetricsPublisher metricsPublisher) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.metricsPublisher = metricsPublisher;
    }

    @Transactional
    public ServiceOrderResponse execute(UUID serviceOrderId, UUID itemId) {
        ServiceOrder order = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder", serviceOrderId));
        ServiceOrderItem item = order.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrderItem", itemId));
        item.finishExecution();
        if (order.allItemsCompleted()) {
            order.finishExecution();
            metricsPublisher.recordServiceOrderStatusDuration(order.getId(), "EXECUTION",
                    Duration.between(order.getExecutionStartedAt(), order.getExecutionFinishedAt()).toMinutes());
        }
        return ServiceOrderResponse.from(serviceOrderRepository.save(order));
    }
}
