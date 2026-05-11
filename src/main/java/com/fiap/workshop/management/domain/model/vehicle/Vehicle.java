package com.fiap.workshop.management.domain.model.vehicle;

import java.util.UUID;

public class Vehicle {

    private UUID id;
    private LicensePlate plate;
    private String brand;
    private String model;
    private int year;
    private String color;
    private UUID customerId;

    public Vehicle(UUID id, LicensePlate plate, String brand, String model, int year, String color, UUID customerId) {
        this.id = id;
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.customerId = customerId;
    }

    public static Vehicle create(LicensePlate plate, String brand, String model, int year, String color, UUID customerId) {
        return new Vehicle(UUID.randomUUID(), plate, brand, model, year, color, customerId);
    }

    public void update(String brand, String model, int year, String color) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
    }

    public UUID getId() { return id; }
    public LicensePlate getPlate() { return plate; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getColor() { return color; }
    public UUID getCustomerId() { return customerId; }
}
