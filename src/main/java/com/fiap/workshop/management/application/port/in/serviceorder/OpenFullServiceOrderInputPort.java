package com.fiap.workshop.management.application.port.in.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.OpenFullServiceOrderCommand;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;

public interface OpenFullServiceOrderInputPort {
    ServiceOrderResponse execute(OpenFullServiceOrderCommand command);
}
