package com.fiap.workshop.management.application.usecase.customer;

import com.fiap.workshop.management.application.dto.customer.CustomerResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.customer.Customer;
import com.fiap.workshop.management.domain.model.customer.Document;
import com.fiap.workshop.management.domain.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("FindCustomerUseCase")
class FindCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    private FindCustomerUseCase useCase;
    private Customer customer;
    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        useCase = new FindCustomerUseCase(customerRepository);
        customer = new Customer(id, "John Doe", new Document("52998224725"),
                "11999999999", "john@example.com", LocalDateTime.now());
    }

    @Test
    @DisplayName("should return customer response when found by id")
    void shouldReturnCustomerResponseWhenFoundById() {
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        CustomerResponse response = useCase.findById(id);

        assertEquals(id, response.id());
        assertEquals("John Doe", response.name());
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when customer not found by id")
    void shouldThrowResourceNotFoundExceptionWhenCustomerNotFoundById() {
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.findById(id));
    }

    @Test
    @DisplayName("should return customer response when found by document")
    void shouldReturnCustomerResponseWhenFoundByDocument() {
        when(customerRepository.findByDocument("52998224725")).thenReturn(Optional.of(customer));

        CustomerResponse response = useCase.findByDocument("52998224725");

        assertEquals(id, response.id());
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when customer not found by document")
    void shouldThrowResourceNotFoundExceptionWhenCustomerNotFoundByDocument() {
        when(customerRepository.findByDocument("52998224725")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.findByDocument("52998224725"));
    }

    @Test
    @DisplayName("should return all customers as response list")
    void shouldReturnAllCustomersAsResponseList() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<CustomerResponse> result = useCase.findAll();

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).id());
    }
}
