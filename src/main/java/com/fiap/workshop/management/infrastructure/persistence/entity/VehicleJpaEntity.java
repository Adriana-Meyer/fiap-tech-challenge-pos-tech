package com.fiap.workshop.management.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicles")
public class VehicleJpaEntity {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", nullable = false)
    private String id;

    @Column(name = "plate_value", length = 8, nullable = false)
    private String plateValue;

    @Column(name = "brand", length = 100, nullable = false)
    private String brand;

    @Column(name = "model", length = 100, nullable = false)
    private String model;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "customer_id", columnDefinition = "CHAR(36)", nullable = false)
    private String customerId;

    protected VehicleJpaEntity() {}

    public VehicleJpaEntity(String id, String plateValue, String brand, String model,
                             int year, String color, String customerId) {
        this.id = id;
        this.plateValue = plateValue;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.customerId = customerId;
    }

    public String getId() { return id; }
    public String getPlateValue() { return plateValue; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getColor() { return color; }
    public String getCustomerId() { return customerId; }
}
