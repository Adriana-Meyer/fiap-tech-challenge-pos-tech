package com.fiap.workshop.management.infrastructure.persistence.adapter;

import com.fiap.workshop.management.domain.model.customer.Customer;
import com.fiap.workshop.management.domain.repository.CustomerRepository;
import com.fiap.workshop.management.infrastructure.persistence.mapper.CustomerMapper;
import com.fiap.workshop.management.infrastructure.persistence.repository.CustomerJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerJpaRepository jpaRepository;
    private final CustomerMapper mapper;

    public CustomerRepositoryAdapter(CustomerJpaRepository jpaRepository, CustomerMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Customer save(Customer customer) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(customer)));
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return jpaRepository.findById(id.toString()).map(mapper::toDomain);
    }

    @Override
    public Optional<Customer> findByDocument(String documentValue) {
        return jpaRepository.findByDocumentValue(documentValue).map(mapper::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id.toString());
    }

    @Override
    public boolean existsByDocument(String documentValue) {
        return jpaRepository.existsByDocumentValue(documentValue);
    }
}
