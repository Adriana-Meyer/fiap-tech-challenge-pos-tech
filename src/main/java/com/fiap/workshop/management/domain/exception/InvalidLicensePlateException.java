package com.fiap.workshop.management.domain.exception;

public class InvalidLicensePlateException extends RuntimeException {

    public InvalidLicensePlateException(String value) {
        super("Invalid license plate: " + value);
    }
}
