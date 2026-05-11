package com.fiap.workshop.management.interfaces.rest;

import com.fiap.workshop.management.application.dto.vehicle.RegisterVehicleCommand;
import com.fiap.workshop.management.application.dto.vehicle.UpdateVehicleCommand;
import com.fiap.workshop.management.application.dto.vehicle.VehicleResponse;
import com.fiap.workshop.management.application.usecase.vehicle.DeleteVehicleUseCase;
import com.fiap.workshop.management.application.usecase.vehicle.FindVehicleUseCase;
import com.fiap.workshop.management.application.usecase.vehicle.RegisterVehicleUseCase;
import com.fiap.workshop.management.application.usecase.vehicle.UpdateVehicleUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class VehicleController {

    private final RegisterVehicleUseCase registerVehicleUseCase;
    private final FindVehicleUseCase findVehicleUseCase;
    private final UpdateVehicleUseCase updateVehicleUseCase;
    private final DeleteVehicleUseCase deleteVehicleUseCase;

    public VehicleController(RegisterVehicleUseCase registerVehicleUseCase,
                              FindVehicleUseCase findVehicleUseCase,
                              UpdateVehicleUseCase updateVehicleUseCase,
                              DeleteVehicleUseCase deleteVehicleUseCase) {
        this.registerVehicleUseCase = registerVehicleUseCase;
        this.findVehicleUseCase = findVehicleUseCase;
        this.updateVehicleUseCase = updateVehicleUseCase;
        this.deleteVehicleUseCase = deleteVehicleUseCase;
    }

    @PostMapping("/api/v1/vehicles")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse register(@Valid @RequestBody RegisterVehicleCommand command) {
        return registerVehicleUseCase.execute(command);
    }

    @GetMapping("/api/v1/vehicles")
    public List<VehicleResponse> findAll() {
        return findVehicleUseCase.findAll();
    }

    @GetMapping("/api/v1/vehicles/{id}")
    public VehicleResponse findById(@PathVariable UUID id) {
        return findVehicleUseCase.findById(id);
    }

    @GetMapping("/api/v1/vehicles/plate/{plate}")
    public VehicleResponse findByPlate(@PathVariable String plate) {
        return findVehicleUseCase.findByPlate(plate);
    }

    @GetMapping("/api/v1/customers/{customerId}/vehicles")
    public List<VehicleResponse> findByCustomer(@PathVariable UUID customerId) {
        return findVehicleUseCase.findByCustomerId(customerId);
    }

    @PutMapping("/api/v1/vehicles/{id}")
    public VehicleResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateVehicleCommand command) {
        return updateVehicleUseCase.execute(id, command);
    }

    @DeleteMapping("/api/v1/vehicles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteVehicleUseCase.execute(id);
    }
}
