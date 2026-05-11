package com.fiap.workshop.management.infrastructure.persistence.adapter;

import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.catalog.ServiceType;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderItem;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderStatus;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.model.supply.Supply;
import com.fiap.workshop.management.domain.model.supply.SupplyType;
import com.fiap.workshop.management.domain.repository.ServiceOrderRepository;
import com.fiap.workshop.management.infrastructure.persistence.entity.ServiceCatalogItemJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.entity.ServiceOrderItemJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.entity.ServiceOrderJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.entity.SupplyJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.repository.ServiceCatalogJpaRepository;
import com.fiap.workshop.management.infrastructure.persistence.repository.ServiceOrderJpaRepository;
import com.fiap.workshop.management.infrastructure.persistence.repository.SupplyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ServiceOrderRepositoryAdapter implements ServiceOrderRepository {

    private final ServiceOrderJpaRepository serviceOrderJpaRepository;
    private final ServiceCatalogJpaRepository serviceCatalogJpaRepository;
    private final SupplyJpaRepository supplyJpaRepository;

    public ServiceOrderRepositoryAdapter(ServiceOrderJpaRepository serviceOrderJpaRepository,
                                          ServiceCatalogJpaRepository serviceCatalogJpaRepository,
                                          SupplyJpaRepository supplyJpaRepository) {
        this.serviceOrderJpaRepository = serviceOrderJpaRepository;
        this.serviceCatalogJpaRepository = serviceCatalogJpaRepository;
        this.supplyJpaRepository = supplyJpaRepository;
    }

    @Override
    public ServiceOrder save(ServiceOrder order) {
        ServiceOrderJpaEntity entity = toEntity(order);
        return toDomain(serviceOrderJpaRepository.save(entity));
    }

    @Override
    public Optional<ServiceOrder> findById(UUID id) {
        return serviceOrderJpaRepository.findById(id.toString()).map(this::toDomain);
    }

    @Override
    public Optional<ServiceOrder> findByOsCode(String osCode) {
        return serviceOrderJpaRepository.findByOsCode(osCode).map(this::toDomain);
    }

    @Override
    public List<ServiceOrder> findAll() {
        return serviceOrderJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public long countByYear(int year) {
        return serviceOrderJpaRepository.countByYear(year);
    }

    @Override
    public List<ServiceOrderItem> findCompletedServiceItems() {
        return serviceOrderJpaRepository.findCompletedServiceItems().stream()
                .map(this::itemToDomain)
                .toList();
    }

    private ServiceOrder toDomain(ServiceOrderJpaEntity e) {
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

    private ServiceOrderItem itemToDomain(ServiceOrderItemJpaEntity e) {
        ServiceCatalogItem service = e.getServiceCatalogItem() != null
                ? catalogToDomain(e.getServiceCatalogItem()) : null;
        Supply supply = e.getSupply() != null
                ? supplyToDomain(e.getSupply()) : null;
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

    private ServiceCatalogItem catalogToDomain(ServiceCatalogItemJpaEntity e) {
        return new ServiceCatalogItem(
                UUID.fromString(e.getId()),
                e.getName(),
                e.getDescription(),
                ServiceType.valueOf(e.getType()),
                Money.of(e.getBasePrice()),
                e.isActive()
        );
    }

    private Supply supplyToDomain(SupplyJpaEntity e) {
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

    private ServiceOrderJpaEntity toEntity(ServiceOrder order) {
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
                    ? serviceCatalogJpaRepository.findById(item.getService().getId().toString()).orElse(null)
                    : null;
            SupplyJpaEntity supplyEntity = item.getSupply() != null
                    ? supplyJpaRepository.findById(item.getSupply().getId().toString()).orElse(null)
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
