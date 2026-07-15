package com.fiap.workshop.management.interfaces.rest;

import com.fiap.workshop.management.application.dto.vehicle.RegisterVehicleCommand;
import com.fiap.workshop.management.application.dto.vehicle.UpdateVehicleCommand;
import com.fiap.workshop.management.application.dto.vehicle.VehicleResponse;
import com.fiap.workshop.management.application.port.in.vehicle.DeleteVehicleInputPort;
import com.fiap.workshop.management.application.port.in.vehicle.FindVehicleInputPort;
import com.fiap.workshop.management.application.port.in.vehicle.RegisterVehicleInputPort;
import com.fiap.workshop.management.application.port.in.vehicle.UpdateVehicleInputPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final RegisterVehicleInputPort registerVehicleUseCase;
    private final FindVehicleInputPort findVehicleUseCase;
    private final UpdateVehicleInputPort updateVehicleUseCase;
    private final DeleteVehicleInputPort deleteVehicleUseCase;

    public VehicleController(RegisterVehicleInputPort registerVehicleUseCase,
                              FindVehicleInputPort findVehicleUseCase,
                              UpdateVehicleInputPort updateVehicleUseCase,
                              DeleteVehicleInputPort deleteVehicleUseCase) {
        this.registerVehicleUseCase = registerVehicleUseCase;
        this.findVehicleUseCase = findVehicleUseCase;
        this.updateVehicleUseCase = updateVehicleUseCase;
        this.deleteVehicleUseCase = deleteVehicleUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse register(@Valid @RequestBody RegisterVehicleCommand command) {
        return registerVehicleUseCase.execute(command);
    }

    @GetMapping
    public List<VehicleResponse> findAll() {
        return findVehicleUseCase.findAll();
    }

    @GetMapping("/{id}")
    public VehicleResponse findById(@PathVariable UUID id) {
        return findVehicleUseCase.findById(id);
    }

    @GetMapping("/plate/{plate}")
    public VehicleResponse findByPlate(@PathVariable String plate) {
        return findVehicleUseCase.findByPlate(plate);
    }

    @PutMapping("/{id}")
    public VehicleResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateVehicleCommand command) {
        return updateVehicleUseCase.execute(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteVehicleUseCase.execute(id);
    }
}
