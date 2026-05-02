package com.fiap.workshop.management.domain.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String resourceName, Object identifier) {
        super(resourceName + " already exists: " + identifier);
    }
}
