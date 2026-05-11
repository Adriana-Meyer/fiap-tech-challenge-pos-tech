package com.fiap.workshop.management.application.usecase.customer;

import com.fiap.workshop.management.domain.exception.ResourceInUseException;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.repository.CustomerRepository;
import com.fiap.workshop.management.domain.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    public DeleteCustomerUseCase(CustomerRepository customerRepository, VehicleRepository vehicleRepository) {
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public void execute(UUID id) {
        if (!customerRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Customer", id);
        }
        if (!vehicleRepository.findByCustomerId(id).isEmpty()) {
            throw new ResourceInUseException("Customer has associated vehicles and cannot be deleted");
        }
        customerRepository.deleteById(id);
    }
}
