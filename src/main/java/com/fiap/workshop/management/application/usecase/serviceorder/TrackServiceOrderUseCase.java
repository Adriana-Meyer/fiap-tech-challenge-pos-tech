package com.fiap.workshop.management.application.usecase.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderTrackingResponse;
import com.fiap.workshop.management.application.port.in.serviceorder.TrackServiceOrderInputPort;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrackServiceOrderUseCase implements TrackServiceOrderInputPort {

    private final ServiceOrderRepository serviceOrderRepository;

    public TrackServiceOrderUseCase(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Transactional(readOnly = true)
    public ServiceOrderTrackingResponse execute(String osCode) {
        return serviceOrderRepository.findByOsCode(osCode)
                .map(ServiceOrderTrackingResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder", osCode));
    }
}
