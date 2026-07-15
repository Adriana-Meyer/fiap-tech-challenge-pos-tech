package com.fiap.workshop.management.infrastructure.persistence.adapter;

import com.fiap.workshop.management.domain.model.customer.Customer;
import com.fiap.workshop.management.domain.model.customer.Document;
import com.fiap.workshop.management.infrastructure.persistence.entity.CustomerJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.mapper.CustomerMapper;
import com.fiap.workshop.management.infrastructure.persistence.repository.CustomerJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryAdapterTest {

    @Mock
    private CustomerJpaRepository jpaRepository;

    @Mock
    private CustomerMapper mapper;

    private CustomerRepositoryAdapter adapter;

    private final UUID id = UUID.randomUUID();
    private Customer customer;
    private CustomerJpaEntity entity;

    @BeforeEach
    void setUp() {
        adapter = new CustomerRepositoryAdapter(jpaRepository, mapper);
        customer = Customer.create("João Silva", new Document("52998224725"), "11999999999", "joao@test.com");
        entity = new CustomerJpaEntity(id.toString(), "52998224725", "CPF", "João Silva", "11999999999", "joao@test.com", LocalDateTime.now());
    }

    @Test
    void shouldSaveAndReturnDomainCustomer() {
        when(mapper.toEntity(customer)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(customer);

        Customer result = adapter.save(customer);

        assertEquals(customer, result);
    }

    @Test
    void shouldFindById() {
        when(jpaRepository.findById(id.toString())).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(customer);

        Optional<Customer> result = adapter.findById(id);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldFindByDocument() {
        when(jpaRepository.findByDocumentValue("52998224725")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(customer);

        Optional<Customer> result = adapter.findByDocument("52998224725");

        assertTrue(result.isPresent());
    }

    @Test
    void shouldFindAll() {
        when(jpaRepository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(customer);

        List<Customer> result = adapter.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void shouldDeleteById() {
        adapter.deleteById(id);

        verify(jpaRepository).deleteById(id.toString());
    }

    @Test
    void shouldCheckExistsByDocument() {
        when(jpaRepository.existsByDocumentValue("52998224725")).thenReturn(true);

        assertTrue(adapter.existsByDocument("52998224725"));
    }
}
