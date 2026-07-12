package com.fiap.workshop.management.application.port.in.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.CreateServiceOrderCommand;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;

public interface CreateServiceOrderInputPort {
    ServiceOrderResponse execute(CreateServiceOrderCommand command);
}
