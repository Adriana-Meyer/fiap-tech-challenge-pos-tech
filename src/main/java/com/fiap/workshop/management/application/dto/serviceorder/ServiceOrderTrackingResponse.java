package com.fiap.workshop.management.application.dto.serviceorder;

import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;

import java.time.LocalDateTime;

public record ServiceOrderTrackingResponse(
        String osCode,
        String status,
        String diagnosisNotes,
        LocalDateTime receivedAt,
        LocalDateTime diagnosisStartedAt,
        LocalDateTime waitingApprovalAt,
        LocalDateTime executionStartedAt,
        LocalDateTime executionFinishedAt,
        LocalDateTime deliveredAt
) {
    public static ServiceOrderTrackingResponse from(ServiceOrder order) {
        return new ServiceOrderTrackingResponse(
                order.getOsCode(),
                order.getStatus().name(),
                order.getDiagnosisNotes(),
                order.getReceivedAt(),
                order.getDiagnosisStartedAt(),
                order.getWaitingApprovalAt(),
                order.getExecutionStartedAt(),
                order.getExecutionFinishedAt(),
                order.getDeliveredAt()
        );
    }
}
