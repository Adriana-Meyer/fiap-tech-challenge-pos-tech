package com.fiap.workshop.management.domain.service;

import java.util.UUID;

public interface MetricsPublisher {
    void recordServiceOrderStatusDuration(UUID serviceOrderId, String status, long durationMinutes);
}
