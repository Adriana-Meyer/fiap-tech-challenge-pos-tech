package com.fiap.workshop.management.infrastructure.persistence.mapper;

import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.catalog.ServiceType;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.infrastructure.persistence.entity.ServiceCatalogItemJpaEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ServiceCatalogItemMapper {

    public ServiceCatalogItem toDomain(ServiceCatalogItemJpaEntity e) {
        return new ServiceCatalogItem(
                UUID.fromString(e.getId()),
                e.getName(),
                e.getDescription(),
                ServiceType.valueOf(e.getType()),
                Money.of(e.getBasePrice()),
                e.isActive()
        );
    }

    public ServiceCatalogItemJpaEntity toEntity(ServiceCatalogItem item) {
        return new ServiceCatalogItemJpaEntity(
                item.getId().toString(),
                item.getName(),
                item.getDescription(),
                item.getType().name(),
                item.getBasePrice().getAmount(),
                item.isActive()
        );
    }
}
