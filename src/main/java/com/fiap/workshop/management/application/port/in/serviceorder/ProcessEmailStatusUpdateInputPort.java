package com.fiap.workshop.management.application.port.in.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.EmailStatusUpdateCommand;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;

public interface ProcessEmailStatusUpdateInputPort {
    ServiceOrderResponse execute(EmailStatusUpdateCommand command);
}
