package com.fiap.workshop.management.application.usecase.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;
import com.fiap.workshop.management.domain.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeliverServiceOrderUseCase {

    private final ServiceOrderRepository serviceOrderRepository;

    public DeliverServiceOrderUseCase(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Transactional
    public ServiceOrderResponse execute(UUID id) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder", id));
        order.deliver();
        return ServiceOrderResponse.from(serviceOrderRepository.save(order));
    }
}
