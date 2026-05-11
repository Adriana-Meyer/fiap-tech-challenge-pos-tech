package com.fiap.workshop.management.application.usecase.vehicle;

import com.fiap.workshop.management.application.dto.vehicle.UpdateVehicleCommand;
import com.fiap.workshop.management.application.dto.vehicle.VehicleResponse;
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
@DisplayName("UpdateVehicleUseCase")
class UpdateVehicleUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;

    private UpdateVehicleUseCase useCase;
    private Vehicle vehicle;
    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        useCase = new UpdateVehicleUseCase(vehicleRepository);
        vehicle = new Vehicle(id, new LicensePlate("ABC1234"), "Toyota", "Corolla", 2020, "Black", UUID.randomUUID());
    }

    @Test
    @DisplayName("should update vehicle and return updated response")
    void shouldUpdateVehicleAndReturnUpdatedResponse() {
        UpdateVehicleCommand command = new UpdateVehicleCommand("Honda", "Civic", 2022, "White");
        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        VehicleResponse response = useCase.execute(id, command);

        assertEquals("Honda", vehicle.getBrand());
        assertEquals("Civic", vehicle.getModel());
        verify(vehicleRepository).save(vehicle);
        assertNotNull(response);
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when vehicle not found")
    void shouldThrowResourceNotFoundExceptionWhenVehicleNotFound() {
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(id, new UpdateVehicleCommand("Honda", "Civic", 2022, "White")));
        verify(vehicleRepository, never()).save(any());
    }
}
