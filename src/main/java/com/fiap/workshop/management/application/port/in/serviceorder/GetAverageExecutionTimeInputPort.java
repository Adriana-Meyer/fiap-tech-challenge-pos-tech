package com.fiap.workshop.management.application.port.in.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.AverageExecutionTimeResponse;

import java.util.List;

public interface GetAverageExecutionTimeInputPort {
    List<AverageExecutionTimeResponse> execute();
}
