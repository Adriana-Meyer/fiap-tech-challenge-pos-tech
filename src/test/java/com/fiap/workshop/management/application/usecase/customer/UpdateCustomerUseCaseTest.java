package com.fiap.workshop.management.application.usecase.customer;

import com.fiap.workshop.management.application.dto.customer.CustomerResponse;
import com.fiap.workshop.management.application.dto.customer.UpdateCustomerCommand;
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
@DisplayName("UpdateCustomerUseCase")
class UpdateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    private UpdateCustomerUseCase useCase;
    private Customer customer;
    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        useCase = new UpdateCustomerUseCase(customerRepository);
        customer = new Customer(id, "John Doe", new Document("52998224725"),
                "11999999999", "john@example.com", LocalDateTime.now());
    }

    @Test
    @DisplayName("should update customer and return updated response")
    void shouldUpdateCustomerAndReturnUpdatedResponse() {
        UpdateCustomerCommand command = new UpdateCustomerCommand("Jane Doe", "11888888888", "jane@example.com");
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        CustomerResponse response = useCase.execute(id, command);

        assertEquals("Jane Doe", customer.getName());
        assertEquals("jane@example.com", customer.getEmail());
        verify(customerRepository).save(customer);
        assertNotNull(response);
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when customer not found")
    void shouldThrowResourceNotFoundExceptionWhenCustomerNotFound() {
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(id, new UpdateCustomerCommand("Jane Doe", null, null)));
        verify(customerRepository, never()).save(any());
    }
}
