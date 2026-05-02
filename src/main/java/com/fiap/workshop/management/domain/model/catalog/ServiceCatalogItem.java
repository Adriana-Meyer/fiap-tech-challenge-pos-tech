package com.fiap.workshop.management.domain.model.catalog;

import com.fiap.workshop.management.domain.model.shared.Money;

import java.util.UUID;

public class ServiceCatalogItem {

    private UUID id;
    private String name;
    private String description;
    private ServiceType type;
    private Money basePrice;
    private boolean active;

    public ServiceCatalogItem(UUID id, String name, String description, ServiceType type, Money basePrice, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.basePrice = basePrice;
        this.active = active;
    }

    public static ServiceCatalogItem create(String name, String description, ServiceType type, Money basePrice) {
        return new ServiceCatalogItem(UUID.randomUUID(), name, description, type, basePrice, true);
    }

    public void update(String name, String description, ServiceType type, Money basePrice) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.basePrice = basePrice;
    }

    public void deactivate() {
        this.active = false;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public ServiceType getType() { return type; }
    public Money getBasePrice() { return basePrice; }
    public boolean isActive() { return active; }
}
