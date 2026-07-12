package com.fiap.workshop.management.application.port.in.customer;

import java.util.UUID;

public interface DeleteCustomerInputPort {
    void execute(UUID id);
}
