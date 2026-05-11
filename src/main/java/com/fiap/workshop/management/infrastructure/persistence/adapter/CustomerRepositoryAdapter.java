package com.fiap.workshop.management.infrastructure.persistence.adapter;

import com.fiap.workshop.management.domain.model.customer.Customer;
import com.fiap.workshop.management.domain.model.customer.Document;
import com.fiap.workshop.management.domain.repository.CustomerRepository;
import com.fiap.workshop.management.infrastructure.persistence.entity.CustomerJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.repository.CustomerJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerJpaRepository jpaRepository;

    public CustomerRepositoryAdapter(CustomerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Customer save(Customer customer) {
        return toDomain(jpaRepository.save(toEntity(customer)));
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return jpaRepository.findById(id.toString()).map(this::toDomain);
    }

    @Override
    public Optional<Customer> findByDocument(String documentValue) {
        return jpaRepository.findByDocumentValue(documentValue).map(this::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id.toString());
    }

    @Override
    public boolean existsByDocument(String documentValue) {
        return jpaRepository.existsByDocumentValue(documentValue);
    }

    private Customer toDomain(CustomerJpaEntity e) {
        return new Customer(
                UUID.fromString(e.getId()),
                e.getName(),
                new Document(e.getDocumentValue()),
                e.getPhone(),
                e.getEmail(),
                e.getCreatedAt()
        );
    }

    private CustomerJpaEntity toEntity(Customer c) {
        return new CustomerJpaEntity(
                c.getId().toString(),
                c.getDocument().getValue(),
                c.getDocument().getType().name(),
                c.getName(),
                c.getPhone(),
                c.getEmail(),
                c.getCreatedAt()
        );
    }
}
