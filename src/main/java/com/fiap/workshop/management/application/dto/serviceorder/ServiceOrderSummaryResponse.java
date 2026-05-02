package com.fiap.workshop.management.application.dto.serviceorder;

import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceOrderSummaryResponse(
        UUID id,
        String osCode,
        String status,
        UUID customerId,
        UUID vehicleId,
        BigDecimal totalAmount,
        LocalDateTime receivedAt,
        LocalDateTime updatedAt
) {
    public static ServiceOrderSummaryResponse from(ServiceOrder order) {
        return new ServiceOrderSummaryResponse(
                order.getId(),
                order.getOsCode(),
                order.getStatus().name(),
                order.getCustomerId(),
                order.getVehicleId(),
                order.getTotalAmount().getAmount(),
                order.getReceivedAt(),
                order.getUpdatedAt()
        );
    }
}
