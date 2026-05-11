package com.fiap.workshop.management.application.usecase.customer;

import com.fiap.workshop.management.application.dto.customer.CustomerResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FindCustomerUseCase {

    private final CustomerRepository customerRepository;

    public FindCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID id) {
        return customerRepository.findById(id)
                .map(CustomerResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
    }

    @Transactional(readOnly = true)
    public CustomerResponse findByDocument(String documentValue) {
        return customerRepository.findByDocument(documentValue)
                .map(CustomerResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", documentValue));
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream().map(CustomerResponse::from).toList();
    }
}
