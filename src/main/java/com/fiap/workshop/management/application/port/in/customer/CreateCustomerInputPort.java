package com.fiap.workshop.management.application.port.in.customer;

import com.fiap.workshop.management.application.dto.customer.CreateCustomerCommand;
import com.fiap.workshop.management.application.dto.customer.CustomerResponse;

public interface CreateCustomerInputPort {
    CustomerResponse execute(CreateCustomerCommand command);
}
