package com.fiap.workshop.management.domain.repository;

import com.fiap.workshop.management.domain.model.vehicle.Vehicle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository {

    Vehicle save(Vehicle vehicle);

    Optional<Vehicle> findById(UUID id);

    Optional<Vehicle> findByPlate(String plateValue);

    List<Vehicle> findByCustomerId(UUID customerId);

    List<Vehicle> findAll();

    void deleteById(UUID id);

    boolean existsByPlate(String plateValue);
}
