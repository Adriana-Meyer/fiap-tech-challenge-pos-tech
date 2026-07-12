package com.fiap.workshop.management.infrastructure.persistence.adapter;

import com.fiap.workshop.management.domain.model.vehicle.Vehicle;
import com.fiap.workshop.management.domain.repository.VehicleRepository;
import com.fiap.workshop.management.infrastructure.persistence.mapper.VehicleMapper;
import com.fiap.workshop.management.infrastructure.persistence.repository.VehicleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class VehicleRepositoryAdapter implements VehicleRepository {

    private final VehicleJpaRepository jpaRepository;
    private final VehicleMapper mapper;

    public VehicleRepositoryAdapter(VehicleJpaRepository jpaRepository, VehicleMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(vehicle)));
    }

    @Override
    public Optional<Vehicle> findById(UUID id) {
        return jpaRepository.findById(id.toString()).map(mapper::toDomain);
    }

    @Override
    public Optional<Vehicle> findByPlate(String plateValue) {
        return jpaRepository.findByPlateValue(plateValue).map(mapper::toDomain);
    }

    @Override
    public List<Vehicle> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId.toString()).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Vehicle> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id.toString());
    }

    @Override
    public boolean existsByPlate(String plateValue) {
        return jpaRepository.existsByPlateValue(plateValue);
    }
}
