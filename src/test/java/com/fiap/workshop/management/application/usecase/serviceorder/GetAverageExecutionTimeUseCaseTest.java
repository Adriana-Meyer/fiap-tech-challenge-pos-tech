package com.fiap.workshop.management.application.usecase.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.AverageExecutionTimeResponse;
import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.catalog.ServiceType;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderItem;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.repository.ServiceOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetAverageExecutionTimeUseCase")
class GetAverageExecutionTimeUseCaseTest {

    @Mock
    private ServiceOrderRepository serviceOrderRepository;

    private GetAverageExecutionTimeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAverageExecutionTimeUseCase(serviceOrderRepository);
    }

    @Test
    @DisplayName("should return empty list when no completed service items exist")
    void shouldReturnEmptyListWhenNoCompletedServiceItemsExist() {
        when(serviceOrderRepository.findCompletedServiceItems()).thenReturn(List.of());

        List<AverageExecutionTimeResponse> result = useCase.execute();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("should return average execution time grouped by service type")
    void shouldReturnAverageExecutionTimeGroupedByServiceType() {
        ServiceCatalogItem catalogItem = ServiceCatalogItem.create(
                "Oil Change", "desc", ServiceType.MECHANICAL, Money.of(100.00));
        UUID orderId = UUID.randomUUID();

        LocalDateTime start = LocalDateTime.now().minusHours(2);
        LocalDateTime finish = LocalDateTime.now();
        ServiceOrderItem item = new ServiceOrderItem(
                UUID.randomUUID(), orderId, catalogItem, null,
                1, Money.of(100.00), Money.of(100.00), start, finish);

        when(serviceOrderRepository.findCompletedServiceItems()).thenReturn(List.of(item));

        List<AverageExecutionTimeResponse> result = useCase.execute();

        assertEquals(1, result.size());
        assertEquals("MECHANICAL", result.get(0).serviceType());
        assertEquals(1, result.get(0).sampleCount());
        assertTrue(result.get(0).averageMinutes() > 0);
    }

    @Test
    @DisplayName("should skip items with no service when computing averages")
    void shouldSkipItemsWithNoServiceWhenComputingAverages() {
        ServiceOrderItem supplyOnlyItem = new ServiceOrderItem(
                UUID.randomUUID(), UUID.randomUUID(), null, null,
                1, Money.of(20.00), Money.of(20.00),
                LocalDateTime.now().minusHours(1), LocalDateTime.now());

        when(serviceOrderRepository.findCompletedServiceItems()).thenReturn(List.of(supplyOnlyItem));

        List<AverageExecutionTimeResponse> result = useCase.execute();

        assertTrue(result.isEmpty());
    }
}
