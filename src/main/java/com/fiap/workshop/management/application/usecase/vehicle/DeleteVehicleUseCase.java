package com.fiap.workshop.management.application.usecase.vehicle;

import com.fiap.workshop.management.application.port.in.vehicle.DeleteVehicleInputPort;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteVehicleUseCase implements DeleteVehicleInputPort {

    private final VehicleRepository vehicleRepository;

    public DeleteVehicleUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public void execute(UUID id) {
        if (!vehicleRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Vehicle", id);
        }
        vehicleRepository.deleteById(id);
    }
}
