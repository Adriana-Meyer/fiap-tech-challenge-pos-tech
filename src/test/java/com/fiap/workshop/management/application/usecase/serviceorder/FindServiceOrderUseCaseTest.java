package com.fiap.workshop.management.application.usecase.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderSummaryResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderStatus;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindServiceOrderUseCase")
class FindServiceOrderUseCaseTest {

    @Mock
    private ServiceOrderRepository serviceOrderRepository;

    private FindServiceOrderUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindServiceOrderUseCase(serviceOrderRepository);
    }

    private ServiceOrder orderWith(String osCode, ServiceOrderStatus status, LocalDateTime receivedAt) {
        return new ServiceOrder(UUID.randomUUID(), osCode, status, UUID.randomUUID(), UUID.randomUUID(),
                List.of(), Money.zero(), null, null, receivedAt, null, null, null, null, null,
                receivedAt, receivedAt);
    }

    @Test
    @DisplayName("should return order response when found by id")
    void shouldReturnOrderResponseWhenFoundById() {
        UUID id = UUID.randomUUID();
        ServiceOrder order = orderWith("OS-2026-00001", ServiceOrderStatus.RECEIVED, LocalDateTime.now());
        when(serviceOrderRepository.findById(id)).thenReturn(Optional.of(order));

        ServiceOrderResponse response = useCase.findById(id);

        assertEquals("OS-2026-00001", response.osCode());
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when order not found by id")
    void shouldThrowResourceNotFoundExceptionWhenOrderNotFoundById() {
        UUID id = UUID.randomUUID();
        when(serviceOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.findById(id));
    }

    @Test
    @DisplayName("should exclude FINISHED and DELIVERED orders by default")
    void shouldExcludeFinishedAndDeliveredOrdersByDefault() {
        LocalDateTime now = LocalDateTime.now();
        ServiceOrder received = orderWith("OS-RECEIVED", ServiceOrderStatus.RECEIVED, now);
        ServiceOrder finished = orderWith("OS-FINISHED", ServiceOrderStatus.FINISHED, now);
        ServiceOrder delivered = orderWith("OS-DELIVERED", ServiceOrderStatus.DELIVERED, now);
        when(serviceOrderRepository.findAll()).thenReturn(List.of(received, finished, delivered));

        List<ServiceOrderSummaryResponse> result = useCase.findAll(false);

        assertEquals(1, result.size());
        assertEquals("OS-RECEIVED", result.get(0).osCode());
    }

    @Test
    @DisplayName("should include FINISHED and DELIVERED orders when includeCompleted is true")
    void shouldIncludeFinishedAndDeliveredOrdersWhenIncludeCompletedIsTrue() {
        LocalDateTime now = LocalDateTime.now();
        ServiceOrder received = orderWith("OS-RECEIVED", ServiceOrderStatus.RECEIVED, now);
        ServiceOrder finished = orderWith("OS-FINISHED", ServiceOrderStatus.FINISHED, now);
        when(serviceOrderRepository.findAll()).thenReturn(List.of(received, finished));

        List<ServiceOrderSummaryResponse> result = useCase.findAll(true);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("should sort by status with most-advanced first, then oldest first within same status")
    void shouldSortByStatusMostAdvancedFirstThenOldestFirst() {
        LocalDateTime older = LocalDateTime.now().minusDays(2);
        LocalDateTime newer = LocalDateTime.now().minusDays(1);
        ServiceOrder received = orderWith("OS-RECEIVED", ServiceOrderStatus.RECEIVED, older);
        ServiceOrder inExecutionNewer = orderWith("OS-EXEC-NEWER", ServiceOrderStatus.IN_EXECUTION, newer);
        ServiceOrder inExecutionOlder = orderWith("OS-EXEC-OLDER", ServiceOrderStatus.IN_EXECUTION, older);
        ServiceOrder waitingApproval = orderWith("OS-WAITING", ServiceOrderStatus.WAITING_APPROVAL, older);
        when(serviceOrderRepository.findAll())
                .thenReturn(List.of(received, inExecutionNewer, waitingApproval, inExecutionOlder));

        List<ServiceOrderSummaryResponse> result = useCase.findAll(false);

        assertEquals(List.of("OS-EXEC-OLDER", "OS-EXEC-NEWER", "OS-WAITING", "OS-RECEIVED"),
                result.stream().map(ServiceOrderSummaryResponse::osCode).toList());
    }
}
