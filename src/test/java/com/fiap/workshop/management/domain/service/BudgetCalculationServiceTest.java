package com.fiap.workshop.management.domain.service;

import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.catalog.ServiceType;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderItem;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.model.supply.Supply;
import com.fiap.workshop.management.domain.model.supply.SupplyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BudgetCalculationService")
class BudgetCalculationServiceTest {

    private final BudgetCalculationService service = new BudgetCalculationService();
    private final UUID orderId = UUID.randomUUID();

    @Test
    @DisplayName("should return zero when item list is empty")
    void shouldReturnZeroWhenItemListIsEmpty() {
        assertEquals(Money.zero(), service.calculate(List.of()));
    }

    @Test
    @DisplayName("should return item subtotal when list has a single service item")
    void shouldReturnItemSubtotalWhenListHasSingleServiceItem() {
        ServiceCatalogItem catalogItem = ServiceCatalogItem.create(
                "Oil Change", "desc", ServiceType.MECHANICAL, Money.of(100.00));
        ServiceOrderItem item = ServiceOrderItem.createServiceItem(orderId, catalogItem, 2);

        assertEquals(Money.of(200.00), service.calculate(List.of(item)));
    }

    @Test
    @DisplayName("should return item subtotal when list has a single supply item")
    void shouldReturnItemSubtotalWhenListHasSingleSupplyItem() {
        Supply supply = Supply.create("P001", "Oil Filter", "desc", SupplyType.PART, Money.of(30.00), 5);
        ServiceOrderItem item = ServiceOrderItem.createSupplyItem(orderId, supply, 3);

        assertEquals(Money.of(90.00), service.calculate(List.of(item)));
    }

    @Test
    @DisplayName("should return sum of all subtotals when list has multiple items")
    void shouldReturnSumOfAllSubtotalsWhenListHasMultipleItems() {
        ServiceCatalogItem catalogItem = ServiceCatalogItem.create(
                "Oil Change", "desc", ServiceType.MECHANICAL, Money.of(100.00));
        Supply supply = Supply.create("P001", "Oil Filter", "desc", SupplyType.PART, Money.of(30.00), 5);

        List<ServiceOrderItem> items = List.of(
                ServiceOrderItem.createServiceItem(orderId, catalogItem, 2),  // 200.00
                ServiceOrderItem.createSupplyItem(orderId, supply, 3)          // 90.00
        );

        assertEquals(Money.of(290.00), service.calculate(items));
    }
}
