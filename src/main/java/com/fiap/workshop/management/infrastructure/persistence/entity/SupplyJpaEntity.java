package com.fiap.workshop.management.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "supplies")
public class SupplyJpaEntity {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", nullable = false)
    private String id;

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "type", length = 20, nullable = false)
    private String type;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "minimum_stock", nullable = false)
    private int minimumStock;

    protected SupplyJpaEntity() {}

    public SupplyJpaEntity(String id, String code, String name, String description,
                            String type, BigDecimal unitPrice, int stockQuantity, int minimumStock) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.type = type;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.minimumStock = minimumStock;
    }

    public String getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getStockQuantity() { return stockQuantity; }
    public int getMinimumStock() { return minimumStock; }
}
