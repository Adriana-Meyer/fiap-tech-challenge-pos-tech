package com.fiap.workshop.management.application.dto.serviceorder;

import java.util.UUID;

public record AverageExecutionTimeResponse(
        UUID serviceId,
        String serviceName,
        String serviceType,
        double averageMinutes,
        double averageHours,
        long sampleCount
) {}
