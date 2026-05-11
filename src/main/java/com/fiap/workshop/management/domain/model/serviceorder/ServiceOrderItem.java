package com.fiap.workshop.management.domain.model.serviceorder;

import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.model.supply.Supply;

import java.time.LocalDateTime;
import java.util.UUID;

public class ServiceOrderItem {

    private UUID id;
    private UUID serviceOrderId;
    private ServiceCatalogItem service;
    private Supply supply;
    private int quantity;
    private Money unitPrice;
    private Money subtotal;
    private LocalDateTime executionStartedAt;
    private LocalDateTime executionFinishedAt;

    public ServiceOrderItem(UUID id, UUID serviceOrderId, ServiceCatalogItem service, Supply supply,
                            int quantity, Money unitPrice, Money subtotal,
                            LocalDateTime executionStartedAt, LocalDateTime executionFinishedAt) {
        this.id = id;
        this.serviceOrderId = serviceOrderId;
        this.service = service;
        this.supply = supply;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.executionStartedAt = executionStartedAt;
        this.executionFinishedAt = executionFinishedAt;
    }

    public static ServiceOrderItem createServiceItem(UUID serviceOrderId, ServiceCatalogItem service, int quantity) {
        Money unitPrice = service.getBasePrice();
        return new ServiceOrderItem(UUID.randomUUID(), serviceOrderId, service, null,
                quantity, unitPrice, unitPrice.multiply(quantity), null, null);
    }

    public static ServiceOrderItem createSupplyItem(UUID serviceOrderId, Supply supply, int quantity) {
        Money unitPrice = supply.getUnitPrice();
        return new ServiceOrderItem(UUID.randomUUID(), serviceOrderId, null, supply,
                quantity, unitPrice, unitPrice.multiply(quantity), null, null);
    }

    public void startExecution() {
        this.executionStartedAt = LocalDateTime.now();
    }

    public void finishExecution() {
        this.executionFinishedAt = LocalDateTime.now();
    }

    public boolean isExecutionCompleted() {
        return this.executionFinishedAt != null;
    }

    public UUID getId() { return id; }
    public UUID getServiceOrderId() { return serviceOrderId; }
    public ServiceCatalogItem getService() { return service; }
    public Supply getSupply() { return supply; }
    public int getQuantity() { return quantity; }
    public Money getUnitPrice() { return unitPrice; }
    public Money getSubtotal() { return subtotal; }
    public LocalDateTime getExecutionStartedAt() { return executionStartedAt; }
    public LocalDateTime getExecutionFinishedAt() { return executionFinishedAt; }
}
