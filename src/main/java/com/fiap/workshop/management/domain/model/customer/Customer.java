package com.fiap.workshop.management.domain.model.customer;

import java.time.LocalDateTime;
import java.util.UUID;

public class Customer {

    private UUID id;
    private String name;
    private Document document;
    private String phone;
    private String email;
    private LocalDateTime createdAt;

    public Customer(UUID id, String name, Document document, String phone, String email, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
    }

    public static Customer create(String name, Document document, String phone, String email) {
        return new Customer(UUID.randomUUID(), name, document, phone, email, LocalDateTime.now());
    }

    public void update(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public Document getDocument() { return document; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
