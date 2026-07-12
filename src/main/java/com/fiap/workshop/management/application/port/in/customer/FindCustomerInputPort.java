package com.fiap.workshop.management.application.port.in.customer;

import com.fiap.workshop.management.application.dto.customer.CustomerResponse;

import java.util.List;
import java.util.UUID;

public interface FindCustomerInputPort {
    CustomerResponse findById(UUID id);
    CustomerResponse findByDocument(String documentValue);
    List<CustomerResponse> findAll();
}
