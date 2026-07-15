package com.fiap.workshop.management.application.port.in.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.AddItemCommand;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;

import java.util.UUID;

public interface AddItemToServiceOrderInputPort {
    ServiceOrderResponse execute(UUID serviceOrderId, AddItemCommand command);
}
