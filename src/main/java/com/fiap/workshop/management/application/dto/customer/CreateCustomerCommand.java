package com.fiap.workshop.management.application.dto.customer;

import jakarta.validation.constraints.NotBlank;

public record CreateCustomerCommand(
        @NotBlank String name,
        @NotBlank String documentNumber,
        String phone,
        String email
) {}
