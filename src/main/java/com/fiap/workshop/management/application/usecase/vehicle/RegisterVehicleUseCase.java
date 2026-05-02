package com.fiap.workshop.management.application.usecase.vehicle;

import com.fiap.workshop.management.application.dto.vehicle.RegisterVehicleCommand;
import com.fiap.workshop.management.application.dto.vehicle.VehicleResponse;
import com.fiap.workshop.management.domain.exception.DuplicateResourceException;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.vehicle.LicensePlate;
import com.fiap.workshop.management.domain.model.vehicle.Vehicle;
import com.fiap.workshop.management.domain.repository.CustomerRepository;
import com.fiap.workshop.management.domain.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterVehicleUseCase {

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    public RegisterVehicleUseCase(VehicleRepository vehicleRepository, CustomerRepository customerRepository) {
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public VehicleResponse execute(RegisterVehicleCommand command) {
        LicensePlate plate = new LicensePlate(command.plate());
        if (vehicleRepository.existsByPlate(plate.getValue())) {
            throw new DuplicateResourceException("Vehicle", command.plate());
        }
        if (!customerRepository.findById(command.customerId()).isPresent()) {
            throw new ResourceNotFoundException("Customer", command.customerId());
        }
        Vehicle vehicle = Vehicle.create(plate, command.brand(), command.model(),
                command.year(), command.color(), command.customerId());
        return VehicleResponse.from(vehicleRepository.save(vehicle));
    }
}
