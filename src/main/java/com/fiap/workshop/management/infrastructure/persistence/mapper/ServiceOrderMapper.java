package com.fiap.workshop.management.infrastructure.persistence.mapper;

import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderItem;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderStatus;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.model.supply.Supply;
import com.fiap.workshop.management.infrastructure.persistence.entity.ServiceCatalogItemJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.entity.ServiceOrderItemJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.entity.ServiceOrderJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.entity.SupplyJpaEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ServiceOrderMapper {

    private final ServiceCatalogItemMapper serviceCatalogItemMapper;
    private final SupplyMapper supplyMapper;

    public ServiceOrderMapper(ServiceCatalogItemMapper serviceCatalogItemMapper, SupplyMapper supplyMapper) {
        this.serviceCatalogItemMapper = serviceCatalogItemMapper;
        this.supplyMapper = supplyMapper;
    }

    public ServiceOrder toDomain(ServiceOrderJpaEntity e) {
        List<ServiceOrderItem> items = e.getItems().stream().map(this::itemToDomain).toList();
        return new ServiceOrder(
                UUID.fromString(e.getId()),
                e.getOsCode(),
                ServiceOrderStatus.valueOf(e.getStatus()),
                UUID.fromString(e.getCustomerId()),
                UUID.fromString(e.getVehicleId()),
                items,
                Money.of(e.getTotalAmount()),
                e.getDiagnosisNotes(),
                e.getCustomerComments(),
                e.getReceivedAt(),
                e.getDiagnosisStartedAt(),
                e.getWaitingApprovalAt(),
                e.getExecutionStartedAt(),
                e.getExecutionFinishedAt(),
                e.getDeliveredAt(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    public ServiceOrderItem itemToDomain(ServiceOrderItemJpaEntity e) {
        ServiceCatalogItem service = e.getServiceCatalogItem() != null
                ? serviceCatalogItemMapper.toDomain(e.getServiceCatalogItem()) : null;
        Supply supply = e.getSupply() != null
                ? supplyMapper.toDomain(e.getSupply()) : null;
        return new ServiceOrderItem(
                UUID.fromString(e.getId()),
                UUID.fromString(e.getServiceOrder().getId()),
                service,
                supply,
                e.getQuantity(),
                Money.of(e.getUnitPrice()),
                Money.of(e.getSubtotal()),
                e.getExecutionStartedAt(),
                e.getExecutionFinishedAt()
        );
    }

    public ServiceOrderJpaEntity toEntity(ServiceOrder order, EntityManager entityManager) {
        ServiceOrderJpaEntity entity = new ServiceOrderJpaEntity(
                order.getId().toString(),
                order.getOsCode(),
                order.getStatus().name(),
                order.getCustomerId().toString(),
                order.getVehicleId().toString(),
                order.getTotalAmount().getAmount(),
                order.getDiagnosisNotes(),
                order.getCustomerComments(),
                order.getReceivedAt(),
                order.getDiagnosisStartedAt(),
                order.getWaitingApprovalAt(),
                order.getExecutionStartedAt(),
                order.getExecutionFinishedAt(),
                order.getDeliveredAt(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );

        for (ServiceOrderItem item : order.getItems()) {
            ServiceCatalogItemJpaEntity catalogEntity = item.getService() != null
                    ? entityManager.getReference(ServiceCatalogItemJpaEntity.class, item.getService().getId().toString())
                    : null;
            SupplyJpaEntity supplyEntity = item.getSupply() != null
                    ? entityManager.getReference(SupplyJpaEntity.class, item.getSupply().getId().toString())
                    : null;
            entity.addItem(new ServiceOrderItemJpaEntity(
                    item.getId().toString(),
                    entity,
                    catalogEntity,
                    supplyEntity,
                    item.getQuantity(),
                    item.getUnitPrice().getAmount(),
                    item.getSubtotal().getAmount(),
                    item.getExecutionStartedAt(),
                    item.getExecutionFinishedAt()
            ));
        }

        return entity;
    }
}
