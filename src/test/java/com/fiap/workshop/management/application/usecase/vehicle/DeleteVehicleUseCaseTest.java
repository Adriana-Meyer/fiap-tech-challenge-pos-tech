package com.fiap.workshop.management.application.usecase.vehicle;

import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.vehicle.LicensePlate;
import com.fiap.workshop.management.domain.model.vehicle.Vehicle;
import com.fiap.workshop.management.domain.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteVehicleUseCase")
class DeleteVehicleUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;

    private DeleteVehicleUseCase useCase;
    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        useCase = new DeleteVehicleUseCase(vehicleRepository);
    }

    @Test
    @DisplayName("should delete vehicle when found")
    void shouldDeleteVehicleWhenFound() {
        Vehicle vehicle = new Vehicle(id, new LicensePlate("ABC1234"), "Toyota", "Corolla", 2020, "Black", UUID.randomUUID());
        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));

        assertDoesNotThrow(() -> useCase.execute(id));

        verify(vehicleRepository).deleteById(id);
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when vehicle not found")
    void shouldThrowResourceNotFoundExceptionWhenVehicleNotFound() {
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(id));
        verify(vehicleRepository, never()).deleteById(any());
    }
}
