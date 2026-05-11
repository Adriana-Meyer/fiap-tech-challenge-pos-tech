package com.fiap.workshop.management.application.usecase.vehicle;

import com.fiap.workshop.management.application.dto.vehicle.VehicleResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.vehicle.LicensePlate;
import com.fiap.workshop.management.domain.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FindVehicleUseCase {

    private final VehicleRepository vehicleRepository;

    public FindVehicleUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional(readOnly = true)
    public VehicleResponse findById(UUID id) {
        return vehicleRepository.findById(id)
                .map(VehicleResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));
    }

    @Transactional(readOnly = true)
    public VehicleResponse findByPlate(String plate) {
        String normalizedPlate = new LicensePlate(plate).getValue();
        return vehicleRepository.findByPlate(normalizedPlate)
                .map(VehicleResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", plate));
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> findByCustomerId(UUID customerId) {
        return vehicleRepository.findByCustomerId(customerId).stream()
                .map(VehicleResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> findAll() {
        return vehicleRepository.findAll().stream().map(VehicleResponse::from).toList();
    }
}
