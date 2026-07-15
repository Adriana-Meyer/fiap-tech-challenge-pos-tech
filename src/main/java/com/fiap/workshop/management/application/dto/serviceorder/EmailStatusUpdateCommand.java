package com.fiap.workshop.management.application.dto.serviceorder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmailStatusUpdateCommand(
        @NotBlank String osCode,
        @NotNull EmailStatusUpdateSubject subject,
        String body
) {}
