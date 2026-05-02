package com.fiap.workshop.management.application.usecase.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.AverageExecutionTimeResponse;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderItem;
import com.fiap.workshop.management.domain.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GetAverageExecutionTimeUseCase {

    private final ServiceOrderRepository serviceOrderRepository;

    public GetAverageExecutionTimeUseCase(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Transactional(readOnly = true)
    public List<AverageExecutionTimeResponse> execute() {
        List<ServiceOrderItem> items = serviceOrderRepository.findCompletedServiceItems();
        Map<String, List<ServiceOrderItem>> byType = items.stream()
                .filter(i -> i.getService() != null)
                .collect(Collectors.groupingBy(i -> i.getService().getType().name()));

        return byType.entrySet().stream()
                .map(entry -> {
                    List<ServiceOrderItem> typeItems = entry.getValue();
                    double avgMinutes = typeItems.stream()
                            .mapToLong(i -> Duration.between(
                                    i.getExecutionStartedAt(), i.getExecutionFinishedAt()).toMinutes())
                            .average()
                            .orElse(0);
                    return new AverageExecutionTimeResponse(
                            entry.getKey(), avgMinutes, avgMinutes / 60.0, typeItems.size());
                })
                .toList();
    }
}
