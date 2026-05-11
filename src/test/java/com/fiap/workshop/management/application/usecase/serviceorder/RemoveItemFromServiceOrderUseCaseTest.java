package com.fiap.workshop.management.application.usecase.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.catalog.ServiceType;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderItem;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.repository.ServiceOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RemoveItemFromServiceOrderUseCase")
class RemoveItemFromServiceOrderUseCaseTest {

    @Mock
    private ServiceOrderRepository serviceOrderRepository;

    private RemoveItemFromServiceOrderUseCase useCase;
    private final UUID orderId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        useCase = new RemoveItemFromServiceOrderUseCase(serviceOrderRepository);
    }

    @Test
    @DisplayName("should remove item from service order and return updated response")
    void shouldRemoveItemFromServiceOrderAndReturnUpdatedResponse() {
        ServiceOrder order = ServiceOrder.create("OS-2026-00001", UUID.randomUUID(), UUID.randomUUID(), null);
        order.startDiagnosis();
        ServiceCatalogItem catalogItem = ServiceCatalogItem.create("Oil Change", "desc", ServiceType.MECHANICAL, Money.of(100.00));
        ServiceOrderItem item = ServiceOrderItem.createServiceItem(orderId, catalogItem, 1);
        order.addItem(item);
        UUID itemId = item.getId();

        when(serviceOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(serviceOrderRepository.save(order)).thenReturn(order);

        ServiceOrderResponse response = useCase.execute(orderId, itemId);

        assertTrue(order.getItems().stream().noneMatch(i -> i.getId().equals(itemId)));
        verify(serviceOrderRepository).save(order);
        assertNotNull(response);
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when service order not found")
    void shouldThrowResourceNotFoundExceptionWhenServiceOrderNotFound() {
        when(serviceOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(orderId, UUID.randomUUID()));
        verify(serviceOrderRepository, never()).save(any());
    }
}
