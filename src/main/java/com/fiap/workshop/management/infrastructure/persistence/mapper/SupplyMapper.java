package com.fiap.workshop.management.infrastructure.persistence.mapper;

import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.model.supply.Supply;
import com.fiap.workshop.management.domain.model.supply.SupplyType;
import com.fiap.workshop.management.infrastructure.persistence.entity.SupplyJpaEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SupplyMapper {

    public Supply toDomain(SupplyJpaEntity e) {
        return new Supply(
                UUID.fromString(e.getId()),
                e.getCode(),
                e.getName(),
                e.getDescription(),
                SupplyType.valueOf(e.getType()),
                Money.of(e.getUnitPrice()),
                e.getStockQuantity(),
                e.getMinimumStock()
        );
    }

    public SupplyJpaEntity toEntity(Supply s) {
        return new SupplyJpaEntity(
                s.getId().toString(),
                s.getCode(),
                s.getName(),
                s.getDescription(),
                s.getType().name(),
                s.getUnitPrice().getAmount(),
                s.getStockQuantity(),
                s.getMinimumStock()
        );
    }
}
