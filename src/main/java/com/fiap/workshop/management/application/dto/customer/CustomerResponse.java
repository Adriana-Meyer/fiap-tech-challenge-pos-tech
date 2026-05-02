package com.fiap.workshop.management.application.dto.customer;

import com.fiap.workshop.management.domain.model.customer.Customer;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String name,
        String document,
        String documentType,
        String phone,
        String email,
        LocalDateTime createdAt
) {
    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getDocument().formatted(),
                customer.getDocument().getType().name(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getCreatedAt()
        );
    }
}
