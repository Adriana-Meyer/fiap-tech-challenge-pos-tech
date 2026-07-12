package com.fiap.workshop.management.application.usecase.customer;

import com.fiap.workshop.management.application.dto.customer.CustomerResponse;
import com.fiap.workshop.management.application.dto.customer.UpdateCustomerCommand;
import com.fiap.workshop.management.application.port.in.customer.UpdateCustomerInputPort;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.customer.Customer;
import com.fiap.workshop.management.domain.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UpdateCustomerUseCase implements UpdateCustomerInputPort {

    private final CustomerRepository customerRepository;

    public UpdateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponse execute(UUID id, UpdateCustomerCommand command) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        customer.update(command.name(), command.phone(), command.email());
        return CustomerResponse.from(customerRepository.save(customer));
    }
}
