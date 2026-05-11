package com.fiap.workshop.management.domain.exception;

public class InvalidDocumentException extends RuntimeException {

    public InvalidDocumentException(String value) {
        super("Invalid document (CPF/CNPJ): " + value);
    }
}
