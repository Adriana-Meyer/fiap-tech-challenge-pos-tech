package com.fiap.workshop.management.domain.exception;

public class ResourceInUseException extends RuntimeException {

    public ResourceInUseException(String message) {
        super(message);
    }
}
