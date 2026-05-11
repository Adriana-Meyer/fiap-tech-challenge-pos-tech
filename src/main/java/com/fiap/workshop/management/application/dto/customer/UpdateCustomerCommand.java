package com.fiap.workshop.management.application.dto.customer;

import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerCommand(
        @NotBlank String name,
        String phone,
        String email
) {}
