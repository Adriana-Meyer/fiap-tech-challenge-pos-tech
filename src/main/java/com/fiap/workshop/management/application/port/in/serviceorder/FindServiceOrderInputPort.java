package com.fiap.workshop.management.application.port.in.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderSummaryResponse;

import java.util.List;
import java.util.UUID;

public interface FindServiceOrderInputPort {
    ServiceOrderResponse findById(UUID id);
    List<ServiceOrderSummaryResponse> findAll(boolean includeCompleted);
}
