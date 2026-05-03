package com.fiap.workshop.management.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_orders")
public class ServiceOrderJpaEntity {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", nullable = false)
    private String id;

    @Column(name = "os_code", length = 20, nullable = false)
    private String osCode;

    @Column(name = "status", length = 30, nullable = false)
    private String status;

    @Column(name = "customer_id", columnDefinition = "CHAR(36)", nullable = false)
    private String customerId;

    @Column(name = "vehicle_id", columnDefinition = "CHAR(36)", nullable = false)
    private String vehicleId;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "diagnosis_notes", columnDefinition = "TEXT")
    private String diagnosisNotes;

    @Column(name = "customer_comments", columnDefinition = "TEXT")
    private String customerComments;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    @Column(name = "diagnosis_started_at")
    private LocalDateTime diagnosisStartedAt;

    @Column(name = "waiting_approval_at")
    private LocalDateTime waitingApprovalAt;

    @Column(name = "execution_started_at")
    private LocalDateTime executionStartedAt;

    @Column(name = "execution_finished_at")
    private LocalDateTime executionFinishedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ServiceOrderItemJpaEntity> items = new ArrayList<>();

    protected ServiceOrderJpaEntity() {}

    public ServiceOrderJpaEntity(String id, String osCode, String status, String customerId, String vehicleId,
                                  BigDecimal totalAmount, String diagnosisNotes, String customerComments,
                                  LocalDateTime receivedAt, LocalDateTime diagnosisStartedAt,
                                  LocalDateTime waitingApprovalAt, LocalDateTime executionStartedAt,
                                  LocalDateTime executionFinishedAt, LocalDateTime deliveredAt,
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.osCode = osCode;
        this.status = status;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.totalAmount = totalAmount;
        this.diagnosisNotes = diagnosisNotes;
        this.customerComments = customerComments;
        this.receivedAt = receivedAt;
        this.diagnosisStartedAt = diagnosisStartedAt;
        this.waitingApprovalAt = waitingApprovalAt;
        this.executionStartedAt = executionStartedAt;
        this.executionFinishedAt = executionFinishedAt;
        this.deliveredAt = deliveredAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void addItem(ServiceOrderItemJpaEntity item) {
        items.add(item);
    }

    public String getId() { return id; }
    public String getOsCode() { return osCode; }
    public String getStatus() { return status; }
    public String getCustomerId() { return customerId; }
    public String getVehicleId() { return vehicleId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getDiagnosisNotes() { return diagnosisNotes; }
    public String getCustomerComments() { return customerComments; }
    public LocalDateTime getReceivedAt() { return receivedAt; }
    public LocalDateTime getDiagnosisStartedAt() { return diagnosisStartedAt; }
    public LocalDateTime getWaitingApprovalAt() { return waitingApprovalAt; }
    public LocalDateTime getExecutionStartedAt() { return executionStartedAt; }
    public LocalDateTime getExecutionFinishedAt() { return executionFinishedAt; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<ServiceOrderItemJpaEntity> getItems() { return items; }
}
