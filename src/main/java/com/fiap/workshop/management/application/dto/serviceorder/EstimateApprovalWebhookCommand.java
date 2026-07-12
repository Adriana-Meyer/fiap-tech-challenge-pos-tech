package com.fiap.workshop.management.application.dto.serviceorder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EstimateApprovalWebhookCommand(
        @NotBlank String osCode,
        @NotNull EstimateDecision decision
) {}
