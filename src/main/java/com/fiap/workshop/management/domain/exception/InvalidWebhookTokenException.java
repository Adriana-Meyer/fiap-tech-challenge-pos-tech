package com.fiap.workshop.management.domain.exception;

public class InvalidWebhookTokenException extends RuntimeException {

    public InvalidWebhookTokenException(String message) {
        super(message);
    }
}
