package com.fiap.workshop.management.application.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthRequest(
        @NotBlank @Pattern(regexp = "\\d{11}", message = "cpf must contain 11 digits") String cpf,
        @NotBlank String password
) {}
