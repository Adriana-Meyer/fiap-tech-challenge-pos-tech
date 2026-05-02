package com.fiap.workshop.management.application.usecase.customer;

import com.fiap.workshop.management.application.dto.customer.CreateCustomerCommand;
import com.fiap.workshop.management.application.dto.customer.CustomerResponse;
import com.fiap.workshop.management.domain.exception.DuplicateResourceException;
import com.fiap.workshop.management.domain.model.customer.Customer;
import com.fiap.workshop.management.domain.model.customer.Document;
import com.fiap.workshop.management.domain.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateCustomerUseCase {

    private final CustomerRepository customerRepository;

    public CreateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponse execute(CreateCustomerCommand command) {
        Document document = new Document(command.documentNumber());
        if (customerRepository.existsByDocument(document.getValue())) {
            throw new DuplicateResourceException("Customer", command.documentNumber());
        }
        Customer customer = Customer.create(command.name(), document, command.phone(), command.email());
        return CustomerResponse.from(customerRepository.save(customer));
    }
}
