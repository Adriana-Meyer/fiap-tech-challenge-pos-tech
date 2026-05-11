package com.fiap.workshop.management.application.usecase.customer;

import com.fiap.workshop.management.domain.exception.ResourceInUseException;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.customer.Customer;
import com.fiap.workshop.management.domain.model.customer.Document;
import com.fiap.workshop.management.domain.model.vehicle.Vehicle;
import com.fiap.workshop.management.domain.model.vehicle.LicensePlate;
import com.fiap.workshop.management.domain.repository.CustomerRepository;
import com.fiap.workshop.management.domain.repository.VehicleRepository;
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
@DisplayName("DeleteCustomerUseCase")
class DeleteCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    private DeleteCustomerUseCase useCase;
    private final UUID id = UUID.randomUUID();
    private Customer customer;

    @BeforeEach
    void setUp() {
        useCase = new DeleteCustomerUseCase(customerRepository, vehicleRepository);
        customer = new Customer(id, "John Doe", new Document("52998224725"),
                "11999999999", "john@example.com", LocalDateTime.now());
    }

    @Test
    @DisplayName("should delete customer when found and has no vehicles")
    void shouldDeleteCustomerWhenFoundAndHasNoVehicles() {
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(vehicleRepository.findByCustomerId(id)).thenReturn(List.of());

        assertDoesNotThrow(() -> useCase.execute(id));

        verify(customerRepository).deleteById(id);
    }

    @Test
    @DisplayName("should throw ResourceInUseException when customer has associated vehicles")
    void shouldThrowResourceInUseExceptionWhenCustomerHasAssociatedVehicles() {
        Vehicle vehicle = new Vehicle(UUID.randomUUID(), new LicensePlate("ABC1234"),
                "Toyota", "Corolla", 2020, "Silver", id);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(vehicleRepository.findByCustomerId(id)).thenReturn(List.of(vehicle));

        assertThrows(ResourceInUseException.class, () -> useCase.execute(id));

        verify(customerRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when customer not found")
    void shouldThrowResourceNotFoundExceptionWhenCustomerNotFound() {
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(id));
        verify(customerRepository, never()).deleteById(any());
    }
}
