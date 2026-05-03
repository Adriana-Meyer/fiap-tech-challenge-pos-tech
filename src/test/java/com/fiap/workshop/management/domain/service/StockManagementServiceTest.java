package com.fiap.workshop.management.domain.service;

import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.catalog.ServiceType;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderItem;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.model.supply.Supply;
import com.fiap.workshop.management.domain.model.supply.SupplyType;
import com.fiap.workshop.management.domain.repository.SupplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StockManagementService")
class StockManagementServiceTest {

    @Mock
    private SupplyRepository supplyRepository;

    private StockManagementService stockService;
    private final UUID orderId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        stockService = new StockManagementService(supplyRepository);
    }

    @Test
    @DisplayName("should not interact with repository when item has no supply")
    void shouldNotInteractWithRepositoryWhenItemHasNoSupply() {
        ServiceCatalogItem catalogItem = ServiceCatalogItem.create(
                "Oil Change", "desc", ServiceType.MECHANICAL, Money.of(100.00));
        ServiceOrderItem serviceOnly = ServiceOrderItem.createServiceItem(orderId, catalogItem, 1);

        stockService.deductStock(List.of(serviceOnly));

        verifyNoInteractions(supplyRepository);
    }

    @Test
    @DisplayName("should deduct quantity from stock and save when item has supply")
    void shouldDeductQuantityFromStockAndSaveWhenItemHasSupply() {
        Supply supply = new Supply(UUID.randomUUID(), "P001", "Oil Filter", "desc",
                SupplyType.PART, Money.of(20.00), 10, 2);
        ServiceOrderItem supplyItem = ServiceOrderItem.createSupplyItem(orderId, supply, 3);
        when(supplyRepository.findById(supply.getId())).thenReturn(Optional.of(supply));

        stockService.deductStock(List.of(supplyItem));

        assertEquals(7, supply.getStockQuantity());
        verify(supplyRepository).save(supply);
    }

    @Test
    @DisplayName("should allow stock to go negative without throwing exception")
    void shouldAllowStockToGoNegativeWithoutThrowingException() {
        Supply supply = new Supply(UUID.randomUUID(), "P002", "Brake Pad", "desc",
                SupplyType.PART, Money.of(50.00), 2, 5);
        ServiceOrderItem supplyItem = ServiceOrderItem.createSupplyItem(orderId, supply, 5);
        when(supplyRepository.findById(supply.getId())).thenReturn(Optional.of(supply));

        assertDoesNotThrow(() -> stockService.deductStock(List.of(supplyItem)));
        assertEquals(-3, supply.getStockQuantity());
    }

    @Test
    @DisplayName("should throw IllegalStateException when supply is not found in repository")
    void shouldThrowIllegalStateExceptionWhenSupplyIsNotFoundInRepository() {
        Supply supply = new Supply(UUID.randomUUID(), "P003", "Filter", "desc",
                SupplyType.PART, Money.of(10.00), 5, 1);
        ServiceOrderItem supplyItem = ServiceOrderItem.createSupplyItem(orderId, supply, 1);
        when(supplyRepository.findById(supply.getId())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> stockService.deductStock(List.of(supplyItem)));
    }

    @Test
    @DisplayName("should deduct stock only from supply items when list has mixed items")
    void shouldDeductStockOnlyFromSupplyItemsWhenListHasMixedItems() {
        ServiceCatalogItem catalogItem = ServiceCatalogItem.create(
                "Oil Change", "desc", ServiceType.MECHANICAL, Money.of(100.00));
        Supply supply = new Supply(UUID.randomUUID(), "P001", "Oil Filter", "desc",
                SupplyType.PART, Money.of(20.00), 8, 2);

        ServiceOrderItem serviceItem = ServiceOrderItem.createServiceItem(orderId, catalogItem, 1);
        ServiceOrderItem supplyItem = ServiceOrderItem.createSupplyItem(orderId, supply, 2);
        when(supplyRepository.findById(supply.getId())).thenReturn(Optional.of(supply));

        stockService.deductStock(List.of(serviceItem, supplyItem));

        assertEquals(6, supply.getStockQuantity());
        verify(supplyRepository, times(1)).save(supply);
    }
}
