package com.fiap.workshop.management.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_order_items")
public class ServiceOrderItemJpaEntity {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_id", nullable = false)
    private ServiceOrderJpaEntity serviceOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_catalog_item_id")
    private ServiceCatalogItemJpaEntity serviceCatalogItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supply_id")
    private SupplyJpaEntity supply;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "execution_started_at")
    private LocalDateTime executionStartedAt;

    @Column(name = "execution_finished_at")
    private LocalDateTime executionFinishedAt;

    protected ServiceOrderItemJpaEntity() {}

    public ServiceOrderItemJpaEntity(String id, ServiceOrderJpaEntity serviceOrder,
                                      ServiceCatalogItemJpaEntity serviceCatalogItem,
                                      SupplyJpaEntity supply, int quantity,
                                      BigDecimal unitPrice, BigDecimal subtotal,
                                      LocalDateTime executionStartedAt, LocalDateTime executionFinishedAt) {
        this.id = id;
        this.serviceOrder = serviceOrder;
        this.serviceCatalogItem = serviceCatalogItem;
        this.supply = supply;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.executionStartedAt = executionStartedAt;
        this.executionFinishedAt = executionFinishedAt;
    }

    public String getId() { return id; }
    public ServiceOrderJpaEntity getServiceOrder() { return serviceOrder; }
    public ServiceCatalogItemJpaEntity getServiceCatalogItem() { return serviceCatalogItem; }
    public SupplyJpaEntity getSupply() { return supply; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getSubtotal() { return subtotal; }
    public LocalDateTime getExecutionStartedAt() { return executionStartedAt; }
    public LocalDateTime getExecutionFinishedAt() { return executionFinishedAt; }
}
