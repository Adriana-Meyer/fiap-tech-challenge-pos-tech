package com.fiap.workshop.management.domain.exception;

import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(ServiceOrderStatus from, ServiceOrderStatus to) {
        super(String.format("Cannot transition service order from %s to %s", from, to));
    }

    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}
