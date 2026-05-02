package com.fiap.workshop.management.application.usecase.customer;

import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteCustomerUseCase {

    private final CustomerRepository customerRepository;

    public DeleteCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void execute(UUID id) {
        if (!customerRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Customer", id);
        }
        customerRepository.deleteById(id);
    }
}
