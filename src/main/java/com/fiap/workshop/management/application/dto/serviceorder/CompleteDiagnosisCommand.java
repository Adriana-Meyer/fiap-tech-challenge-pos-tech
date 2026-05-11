package com.fiap.workshop.management.application.dto.serviceorder;

import jakarta.validation.constraints.NotBlank;

public record CompleteDiagnosisCommand(
        @NotBlank(message = "Diagnosis notes are required")
        String diagnosisNotes
) {}
