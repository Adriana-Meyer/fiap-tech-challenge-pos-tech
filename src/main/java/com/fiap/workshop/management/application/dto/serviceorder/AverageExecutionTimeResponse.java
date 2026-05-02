package com.fiap.workshop.management.application.dto.serviceorder;

public record AverageExecutionTimeResponse(
        String serviceType,
        double averageMinutes,
        double averageHours,
        long sampleCount
) {}
