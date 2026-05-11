package com.fiap.workshop.management.domain.repository;

import com.fiap.workshop.management.domain.model.customer.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(UUID id);

    Optional<Customer> findByDocument(String documentValue);

    List<Customer> findAll();

    void deleteById(UUID id);

    boolean existsByDocument(String documentValue);
}
