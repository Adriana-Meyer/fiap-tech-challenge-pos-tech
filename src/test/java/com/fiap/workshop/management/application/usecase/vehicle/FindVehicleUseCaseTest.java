package com.fiap.workshop.management.application.usecase.vehicle;

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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindVehicleUseCase")
class FindVehicleUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;

    private FindVehicleUseCase useCase;
    private Vehicle vehicle;
    private final UUID id = UUID.randomUUID();
    private final UUID customerId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        useCase = new FindVehicleUseCase(vehicleRepository);
        vehicle = new Vehicle(id, new LicensePlate("ABC1234"), "Toyota", "Corolla", 2020, "Black", customerId);
    }

    @Test
    @DisplayName("should return vehicle response when found by id")
    void shouldReturnVehicleResponseWhenFoundById() {
        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));

        VehicleResponse response = useCase.findById(id);

        assertEquals(id, response.id());
        assertEquals("Toyota", response.brand());
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when vehicle not found by id")
    void shouldThrowResourceNotFoundExceptionWhenVehicleNotFoundById() {
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.findById(id));
    }

    @Test
    @DisplayName("should return vehicle response when found by plate")
    void shouldReturnVehicleResponseWhenFoundByPlate() {
        when(vehicleRepository.findByPlate("ABC1234")).thenReturn(Optional.of(vehicle));

        VehicleResponse response = useCase.findByPlate("ABC1234");

        assertEquals(id, response.id());
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when vehicle not found by plate")
    void shouldThrowResourceNotFoundExceptionWhenVehicleNotFoundByPlate() {
        when(vehicleRepository.findByPlate("ABC1234")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.findByPlate("ABC1234"));
    }

    @Test
    @DisplayName("should return vehicles by customer id")
    void shouldReturnVehiclesByCustomerId() {
        when(vehicleRepository.findByCustomerId(customerId)).thenReturn(List.of(vehicle));

        List<VehicleResponse> result = useCase.findByCustomerId(customerId);

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).id());
    }

    @Test
    @DisplayName("should return all vehicles as response list")
    void shouldReturnAllVehiclesAsResponseList() {
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));

        List<VehicleResponse> result = useCase.findAll();

        assertEquals(1, result.size());
    }
}
