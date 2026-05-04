package com.fiap.workshop.management.application.usecase.customer;

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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteCustomerUseCase")
class DeleteCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    private DeleteCustomerUseCase useCase;
    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        useCase = new DeleteCustomerUseCase(customerRepository);
    }

    @Test
    @DisplayName("should delete customer when found")
    void shouldDeleteCustomerWhenFound() {
        Customer customer = new Customer(id, "John Doe", new Document("52998224725"),
                "11999999999", "john@example.com", LocalDateTime.now());
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        assertDoesNotThrow(() -> useCase.execute(id));

        verify(customerRepository).deleteById(id);
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when customer not found")
    void shouldThrowResourceNotFoundExceptionWhenCustomerNotFound() {
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(id));
        verify(customerRepository, never()).deleteById(any());
    }
}
