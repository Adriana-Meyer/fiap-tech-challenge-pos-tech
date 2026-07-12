package com.fiap.workshop.management.application.port.in.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.EstimateApprovalWebhookCommand;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;

public interface ProcessEstimateApprovalWebhookInputPort {
    ServiceOrderResponse execute(EstimateApprovalWebhookCommand command);
}
