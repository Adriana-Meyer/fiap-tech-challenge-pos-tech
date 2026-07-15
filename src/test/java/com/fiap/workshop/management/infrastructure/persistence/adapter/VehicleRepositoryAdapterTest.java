package com.fiap.workshop.management.infrastructure.persistence.adapter;

import com.fiap.workshop.management.domain.model.vehicle.LicensePlate;
import com.fiap.workshop.management.domain.model.vehicle.Vehicle;
import com.fiap.workshop.management.infrastructure.persistence.entity.VehicleJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.mapper.VehicleMapper;
import com.fiap.workshop.management.infrastructure.persistence.repository.VehicleJpaRepository;
import org.junit.jupiter.api.BeforeEach;
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
class VehicleRepositoryAdapterTest {

    @Mock
    private VehicleJpaRepository jpaRepository;

    @Mock
    private VehicleMapper mapper;

    private VehicleRepositoryAdapter adapter;

    private final UUID id = UUID.randomUUID();
    private final UUID customerId = UUID.randomUUID();
    private Vehicle vehicle;
    private VehicleJpaEntity entity;

    @BeforeEach
    void setUp() {
        adapter = new VehicleRepositoryAdapter(jpaRepository, mapper);
        vehicle = new Vehicle(id, new LicensePlate("ABC1234"), "Toyota", "Corolla", 2020, "Black", customerId);
        entity = new VehicleJpaEntity(id.toString(), "ABC1234", "Toyota", "Corolla", 2020, "Black", customerId.toString());
    }

    @Test
    void shouldSaveAndReturnDomainVehicle() {
        when(mapper.toEntity(vehicle)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(vehicle);

        Vehicle result = adapter.save(vehicle);

        assertEquals(vehicle, result);
    }

    @Test
    void shouldFindById() {
        when(jpaRepository.findById(id.toString())).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(vehicle);

        assertTrue(adapter.findById(id).isPresent());
    }

    @Test
    void shouldFindByPlate() {
        when(jpaRepository.findByPlateValue("ABC1234")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(vehicle);

        assertTrue(adapter.findByPlate("ABC1234").isPresent());
    }

    @Test
    void shouldFindByCustomerId() {
        when(jpaRepository.findByCustomerId(customerId.toString())).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(vehicle);

        assertEquals(1, adapter.findByCustomerId(customerId).size());
    }

    @Test
    void shouldFindAll() {
        when(jpaRepository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(vehicle);

        assertEquals(1, adapter.findAll().size());
    }

    @Test
    void shouldDeleteById() {
        adapter.deleteById(id);

        verify(jpaRepository).deleteById(id.toString());
    }

    @Test
    void shouldCheckExistsByPlate() {
        when(jpaRepository.existsByPlateValue("ABC1234")).thenReturn(true);

        assertTrue(adapter.existsByPlate("ABC1234"));
    }
}
