package com.fiap.workshop.management.infrastructure.persistence.adapter;

import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.model.supply.Supply;
import com.fiap.workshop.management.domain.model.supply.SupplyType;
import com.fiap.workshop.management.domain.repository.SupplyRepository;
import com.fiap.workshop.management.infrastructure.persistence.entity.SupplyJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.repository.SupplyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SupplyRepositoryAdapter implements SupplyRepository {

    private final SupplyJpaRepository jpaRepository;

    public SupplyRepositoryAdapter(SupplyJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Supply save(Supply supply) {
        return toDomain(jpaRepository.save(toEntity(supply)));
    }

    @Override
    public Optional<Supply> findById(UUID id) {
        return jpaRepository.findById(id.toString()).map(this::toDomain);
    }

    @Override
    public Optional<Supply> findByCode(String code) {
        return jpaRepository.findByCode(code).map(this::toDomain);
    }

    @Override
    public List<Supply> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Supply> findBelowMinimumStock() {
        return jpaRepository.findBelowMinimumStock().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id.toString());
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    private Supply toDomain(SupplyJpaEntity e) {
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

    private SupplyJpaEntity toEntity(Supply s) {
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
