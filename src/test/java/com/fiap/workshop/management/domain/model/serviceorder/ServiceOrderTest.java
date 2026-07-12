package com.fiap.workshop.management.domain.model.serviceorder;

import com.fiap.workshop.management.domain.exception.InvalidStatusTransitionException;
import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.catalog.ServiceType;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.service.BudgetCalculationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ServiceOrder")
class ServiceOrderTest {

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final UUID VEHICLE_ID = UUID.randomUUID();
    private final BudgetCalculationService budgetService = new BudgetCalculationService();

    private ServiceOrder newOrder() {
        return ServiceOrder.create("OS-2026-00001", CUSTOMER_ID, VEHICLE_ID, "Test comments");
    }

    private ServiceOrder inDiagnosis() {
        ServiceOrder order = newOrder();
        order.startDiagnosis();
        return order;
    }

    private ServiceOrder waitingApproval() {
        ServiceOrder order = inDiagnosis();
        order.completeDiagnosis("Diagnosis notes", budgetService);
        return order;
    }

    private ServiceOrder inExecution() {
        ServiceOrder order = waitingApproval();
        order.approveEstimate();
        return order;
    }

    private ServiceCatalogItem catalogItem(double price) {
        return ServiceCatalogItem.create("Oil Change", "desc", ServiceType.MECHANICAL, Money.of(price));
    }

    @Test
    @DisplayName("should initialize with RECEIVED status and zero total when created")
    void shouldInitializeWithReceivedStatusAndZeroTotalWhenCreated() {
        ServiceOrder order = newOrder();

        assertEquals(ServiceOrderStatus.RECEIVED, order.getStatus());
        assertEquals(Money.zero(), order.getTotalAmount());
        assertEquals("OS-2026-00001", order.getOsCode());
        assertEquals(CUSTOMER_ID, order.getCustomerId());
        assertEquals(VEHICLE_ID, order.getVehicleId());
        assertNotNull(order.getId());
        assertNotNull(order.getReceivedAt());
        assertNotNull(order.getCreatedAt());
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    @DisplayName("should transition to IN_DIAGNOSIS when startDiagnosis is called")
    void shouldTransitionToInDiagnosisWhenStartDiagnosisIsCalled() {
        ServiceOrder order = newOrder();
        order.startDiagnosis();

        assertEquals(ServiceOrderStatus.IN_DIAGNOSIS, order.getStatus());
        assertNotNull(order.getDiagnosisStartedAt());
    }

    @Test
    @DisplayName("should throw InvalidStatusTransitionException when startDiagnosis is called and status is not RECEIVED")
    void shouldThrowWhenStartDiagnosisCalledAndStatusIsNotReceived() {
        ServiceOrder order = inDiagnosis();
        assertThrows(InvalidStatusTransitionException.class, order::startDiagnosis);
    }

    @Test
    @DisplayName("should transition to WAITING_APPROVAL when completeDiagnosis is called")
    void shouldTransitionToWaitingApprovalWhenCompleteDiagnosisIsCalled() {
        ServiceOrder order = inDiagnosis();
        order.completeDiagnosis("Oil leak found", budgetService);

        assertEquals(ServiceOrderStatus.WAITING_APPROVAL, order.getStatus());
        assertEquals("Oil leak found", order.getDiagnosisNotes());
        assertNotNull(order.getWaitingApprovalAt());
    }

    @Test
    @DisplayName("should calculate total from items when completeDiagnosis is called")
    void shouldCalculateTotalFromItemsWhenCompleteDiagnosisIsCalled() {
        ServiceOrder order = inDiagnosis();
        order.addItem(ServiceOrderItem.createServiceItem(order.getId(), catalogItem(150.00), 2));

        order.completeDiagnosis("notes", budgetService);

        assertEquals(Money.of(300.00), order.getTotalAmount());
    }

    @Test
    @DisplayName("should throw InvalidStatusTransitionException when completeDiagnosis is called and status is not IN_DIAGNOSIS")
    void shouldThrowWhenCompleteDiagnosisCalledAndStatusIsNotInDiagnosis() {
        ServiceOrder order = newOrder();
        assertThrows(InvalidStatusTransitionException.class,
                () -> order.completeDiagnosis("notes", budgetService));
    }

    @Test
    @DisplayName("should transition to IN_EXECUTION when approveEstimate is called")
    void shouldTransitionToInExecutionWhenApproveEstimateIsCalled() {
        ServiceOrder order = waitingApproval();
        order.approveEstimate();

        assertEquals(ServiceOrderStatus.IN_EXECUTION, order.getStatus());
        assertNotNull(order.getExecutionStartedAt());
    }

    @Test
    @DisplayName("should throw InvalidStatusTransitionException when approveEstimate is called and status is not WAITING_APPROVAL")
    void shouldThrowWhenApproveEstimateCalledAndStatusIsNotWaitingApproval() {
        ServiceOrder order = inDiagnosis();
        assertThrows(InvalidStatusTransitionException.class, order::approveEstimate);
    }

    @Test
    @DisplayName("should transition back to IN_DIAGNOSIS and clear waitingApprovalAt when rejectEstimate is called")
    void shouldTransitionBackToInDiagnosisAndClearWaitingApprovalAtWhenRejectEstimateIsCalled() {
        ServiceOrder order = waitingApproval();
        order.rejectEstimate();

        assertEquals(ServiceOrderStatus.IN_DIAGNOSIS, order.getStatus());
        assertNull(order.getWaitingApprovalAt());
    }

    @Test
    @DisplayName("should throw InvalidStatusTransitionException when rejectEstimate is called and status is not WAITING_APPROVAL")
    void shouldThrowWhenRejectEstimateCalledAndStatusIsNotWaitingApproval() {
        ServiceOrder order = inDiagnosis();
        assertThrows(InvalidStatusTransitionException.class, order::rejectEstimate);
    }

    @Test
    @DisplayName("should transition to FINISHED when finishExecution is called")
    void shouldTransitionToFinishedWhenFinishExecutionIsCalled() {
        ServiceOrder order = inExecution();
        order.finishExecution();

        assertEquals(ServiceOrderStatus.FINISHED, order.getStatus());
        assertNotNull(order.getExecutionFinishedAt());
    }

    @Test
    @DisplayName("should throw InvalidStatusTransitionException when finishExecution is called and status is not IN_EXECUTION")
    void shouldThrowWhenFinishExecutionCalledAndStatusIsNotInExecution() {
        ServiceOrder order = waitingApproval();
        assertThrows(InvalidStatusTransitionException.class, order::finishExecution);
    }

    @Test
    @DisplayName("should transition to DELIVERED when deliver is called")
    void shouldTransitionToDeliveredWhenDeliverIsCalled() {
        ServiceOrder order = inExecution();
        order.finishExecution();
        order.deliver();

        assertEquals(ServiceOrderStatus.DELIVERED, order.getStatus());
        assertNotNull(order.getDeliveredAt());
    }

    @Test
    @DisplayName("should throw InvalidStatusTransitionException when deliver is called and status is not FINISHED")
    void shouldThrowWhenDeliverCalledAndStatusIsNotFinished() {
        ServiceOrder order = inExecution();
        assertThrows(InvalidStatusTransitionException.class, order::deliver);
    }

    @Test
    @DisplayName("should add item to list when addItem is called and status is IN_DIAGNOSIS")
    void shouldAddItemToListWhenAddItemCalledAndStatusIsInDiagnosis() {
        ServiceOrder order = inDiagnosis();
        order.addItem(ServiceOrderItem.createServiceItem(order.getId(), catalogItem(100.00), 1));
        assertEquals(1, order.getItems().size());
    }

    @Test
    @DisplayName("should add item to list when addItem is called and status is RECEIVED")
    void shouldAddItemToListWhenAddItemCalledAndStatusIsReceived() {
        ServiceOrder order = newOrder();
        order.addItem(ServiceOrderItem.createServiceItem(order.getId(), catalogItem(100.00), 1));
        assertEquals(1, order.getItems().size());
    }

    @Test
    @DisplayName("should throw InvalidStatusTransitionException when addItem is called and status is not RECEIVED or IN_DIAGNOSIS")
    void shouldThrowWhenAddItemCalledAndStatusIsNotReceivedOrInDiagnosis() {
        ServiceOrder order = waitingApproval();
        ServiceOrderItem item = ServiceOrderItem.createServiceItem(order.getId(), catalogItem(100.00), 1);
        assertThrows(InvalidStatusTransitionException.class, () -> order.addItem(item));
    }

    @Test
    @DisplayName("should remove item from list when removeItem is called and status is IN_DIAGNOSIS")
    void shouldRemoveItemFromListWhenRemoveItemCalledAndStatusIsInDiagnosis() {
        ServiceOrder order = inDiagnosis();
        ServiceOrderItem item = ServiceOrderItem.createServiceItem(order.getId(), catalogItem(100.00), 1);
        order.addItem(item);
        order.removeItem(item.getId());
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    @DisplayName("should throw InvalidStatusTransitionException when removeItem is called and status is not IN_DIAGNOSIS")
    void shouldThrowWhenRemoveItemCalledAndStatusIsNotInDiagnosis() {
        ServiceOrder order = newOrder();
        assertThrows(InvalidStatusTransitionException.class, () -> order.removeItem(UUID.randomUUID()));
    }

    @Test
    @DisplayName("should return false when allItemsCompleted is called and items list is empty")
    void shouldReturnFalseWhenAllItemsCompletedCalledAndItemsListIsEmpty() {
        ServiceOrder order = inDiagnosis();
        assertFalse(order.allItemsCompleted());
    }

    @Test
    @DisplayName("should return false when allItemsCompleted is called and some item is not finished")
    void shouldReturnFalseWhenAllItemsCompletedCalledAndSomeItemIsNotFinished() {
        ServiceOrder order = inDiagnosis();
        order.addItem(ServiceOrderItem.createServiceItem(order.getId(), catalogItem(100.00), 1));
        assertFalse(order.allItemsCompleted());
    }

    @Test
    @DisplayName("should return true when allItemsCompleted is called and all items are finished")
    void shouldReturnTrueWhenAllItemsCompletedCalledAndAllItemsAreFinished() {
        ServiceOrder order = inDiagnosis();
        ServiceOrderItem item = ServiceOrderItem.createServiceItem(order.getId(), catalogItem(100.00), 1);
        order.addItem(item);
        order.getItems().get(0).finishExecution();
        assertTrue(order.allItemsCompleted());
    }

    @Test
    @DisplayName("should return unmodifiable list when getItems is called")
    void shouldReturnUnmodifiableListWhenGetItemsIsCalled() {
        ServiceOrder order = inDiagnosis();
        assertThrows(UnsupportedOperationException.class, () -> order.getItems().add(null));
    }
}
