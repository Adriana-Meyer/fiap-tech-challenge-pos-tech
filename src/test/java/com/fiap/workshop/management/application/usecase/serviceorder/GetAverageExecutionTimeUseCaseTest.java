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
    @DisplayName("should return average execution time grouped by individual service")
    void shouldReturnAverageExecutionTimeGroupedByIndividualService() {
        ServiceCatalogItem oilChange = ServiceCatalogItem.create(
                "Oil Change", "desc", ServiceType.MECHANICAL, Money.of(150.00));
        ServiceCatalogItem brakeCheck = ServiceCatalogItem.create(
                "Brake Check", "desc", ServiceType.MECHANICAL, Money.of(200.00));
        UUID orderId = UUID.randomUUID();

        LocalDateTime now = LocalDateTime.now();
        ServiceOrderItem item1 = new ServiceOrderItem(UUID.randomUUID(), orderId, oilChange, null,
                1, Money.of(150.00), Money.of(150.00), now.minusMinutes(45), now);
        ServiceOrderItem item2 = new ServiceOrderItem(UUID.randomUUID(), orderId, oilChange, null,
                1, Money.of(150.00), Money.of(150.00), now.minusMinutes(15), now);
        ServiceOrderItem item3 = new ServiceOrderItem(UUID.randomUUID(), orderId, brakeCheck, null,
                1, Money.of(200.00), Money.of(200.00), now.minusMinutes(30), now);

        when(serviceOrderRepository.findCompletedServiceItems()).thenReturn(List.of(item1, item2, item3));

        List<AverageExecutionTimeResponse> result = useCase.execute();

        assertEquals(2, result.size());

        AverageExecutionTimeResponse oilResult = result.stream()
                .filter(r -> r.serviceName().equals("Oil Change")).findFirst().orElseThrow();
        assertEquals(oilChange.getId(), oilResult.serviceId());
        assertEquals("MECHANICAL", oilResult.serviceType());
        assertEquals(30.0, oilResult.averageMinutes());
        assertEquals(2, oilResult.sampleCount());

        AverageExecutionTimeResponse brakeResult = result.stream()
                .filter(r -> r.serviceName().equals("Brake Check")).findFirst().orElseThrow();
        assertEquals(30.0, brakeResult.averageMinutes());
        assertEquals(1, brakeResult.sampleCount());
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
