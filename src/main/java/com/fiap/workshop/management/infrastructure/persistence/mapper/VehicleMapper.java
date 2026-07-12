package com.fiap.workshop.management.infrastructure.persistence.mapper;

import com.fiap.workshop.management.domain.model.vehicle.LicensePlate;
import com.fiap.workshop.management.domain.model.vehicle.Vehicle;
import com.fiap.workshop.management.infrastructure.persistence.entity.VehicleJpaEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VehicleMapper {

    public Vehicle toDomain(VehicleJpaEntity e) {
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

    public VehicleJpaEntity toEntity(Vehicle v) {
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
