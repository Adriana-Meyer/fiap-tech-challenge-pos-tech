package com.fiap.workshop.management.application.port.in.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;

import java.util.UUID;

public interface RemoveItemFromServiceOrderInputPort {
    ServiceOrderResponse execute(UUID serviceOrderId, UUID itemId);
}
