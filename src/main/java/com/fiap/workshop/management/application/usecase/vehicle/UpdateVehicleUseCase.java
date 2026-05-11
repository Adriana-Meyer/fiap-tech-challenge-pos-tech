package com.fiap.workshop.management.application.usecase.vehicle;

import com.fiap.workshop.management.application.dto.vehicle.UpdateVehicleCommand;
import com.fiap.workshop.management.application.dto.vehicle.VehicleResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.vehicle.Vehicle;
import com.fiap.workshop.management.domain.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UpdateVehicleUseCase {

    private final VehicleRepository vehicleRepository;

    public UpdateVehicleUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public VehicleResponse execute(UUID id, UpdateVehicleCommand command) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));
        vehicle.update(command.brand(), command.model(), command.year(), command.color());
        return VehicleResponse.from(vehicleRepository.save(vehicle));
    }
}
