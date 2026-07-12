package com.fiap.workshop.management.application.port.in.customer;

import com.fiap.workshop.management.application.dto.customer.CustomerResponse;
import com.fiap.workshop.management.application.dto.customer.UpdateCustomerCommand;

import java.util.UUID;

public interface UpdateCustomerInputPort {
    CustomerResponse execute(UUID id, UpdateCustomerCommand command);
}
