package com.fiap.workshop.management.infrastructure.persistence.adapter;

import com.fiap.workshop.management.domain.model.vehicle.LicensePlate;
import com.fiap.workshop.management.domain.model.vehicle.Vehicle;
import com.fiap.workshop.management.domain.repository.VehicleRepository;
import com.fiap.workshop.management.infrastructure.persistence.entity.VehicleJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.repository.VehicleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class VehicleRepositoryAdapter implements VehicleRepository {

    private final VehicleJpaRepository jpaRepository;

    public VehicleRepositoryAdapter(VehicleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return toDomain(jpaRepository.save(toEntity(vehicle)));
    }

    @Override
    public Optional<Vehicle> findById(UUID id) {
        return jpaRepository.findById(id.toString()).map(this::toDomain);
    }

    @Override
    public Optional<Vehicle> findByPlate(String plateValue) {
        return jpaRepository.findByPlateValue(plateValue).map(this::toDomain);
    }

    @Override
    public List<Vehicle> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId.toString()).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Vehicle> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id.toString());
    }

    @Override
    public boolean existsByPlate(String plateValue) {
        return jpaRepository.existsByPlateValue(plateValue);
    }

    private Vehicle toDomain(VehicleJpaEntity e) {
        return new Vehicle(
                UUID.fromString(e.getId()),
                new LicensePlate(e.getPlateValue()),
                e.getBrand(),
                e.getModel(),
                e.getYear(),
                e.getColor(),
                UUID.fromString(e.getCustomerId())
        );
    }

    private VehicleJpaEntity toEntity(Vehicle v) {
        return new VehicleJpaEntity(
                v.getId().toString(),
                v.getPlate().getValue(),
                v.getBrand(),
                v.getModel(),
                v.getYear(),
                v.getColor(),
                v.getCustomerId().toString()
        );
    }
}
