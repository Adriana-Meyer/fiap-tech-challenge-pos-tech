package com.fiap.workshop.management.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class CustomerJpaEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "document_value", length = 14, nullable = false)
    private String documentValue;

    @Column(name = "document_type", length = 10, nullable = false)
    private String documentType;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected CustomerJpaEntity() {}

    public CustomerJpaEntity(String id, String documentValue, String documentType,
                              String name, String phone, String email, LocalDateTime createdAt) {
        this.id = id;
        this.documentValue = documentValue;
        this.documentType = documentType;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getDocumentValue() { return documentValue; }
    public String getDocumentType() { return documentType; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
