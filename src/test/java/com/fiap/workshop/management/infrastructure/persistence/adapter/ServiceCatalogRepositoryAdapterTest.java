package com.fiap.workshop.management.infrastructure.persistence.adapter;

import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.catalog.ServiceType;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.infrastructure.persistence.entity.ServiceCatalogItemJpaEntity;
import com.fiap.workshop.management.infrastructure.persistence.mapper.ServiceCatalogItemMapper;
import com.fiap.workshop.management.infrastructure.persistence.repository.ServiceCatalogJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceCatalogRepositoryAdapterTest {

    @Mock
    private ServiceCatalogJpaRepository jpaRepository;

    @Mock
    private ServiceCatalogItemMapper mapper;

    private ServiceCatalogRepositoryAdapter adapter;

    private final UUID id = UUID.randomUUID();
    private ServiceCatalogItem item;
    private ServiceCatalogItemJpaEntity entity;

    @BeforeEach
    void setUp() {
        adapter = new ServiceCatalogRepositoryAdapter(jpaRepository, mapper);
        item = ServiceCatalogItem.create("Oil Change", "desc", ServiceType.MECHANICAL, Money.of(BigDecimal.valueOf(150)));
        entity = new ServiceCatalogItemJpaEntity(id.toString(), "Oil Change", "desc", "MECHANICAL", BigDecimal.valueOf(150), true);
    }

    @Test
    void shouldSaveAndReturnDomainItem() {
        when(mapper.toEntity(item)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(item);

        ServiceCatalogItem result = adapter.save(item);

        assertEquals(item, result);
    }

    @Test
    void shouldFindById() {
        when(jpaRepository.findById(id.toString())).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(item);

        assertTrue(adapter.findById(id).isPresent());
    }

    @Test
    void shouldFindAll() {
        when(jpaRepository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(item);

        assertEquals(1, adapter.findAll().size());
    }

    @Test
    void shouldFindAllActive() {
        when(jpaRepository.findByActiveTrue()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(item);

        assertEquals(1, adapter.findAllActive().size());
    }
}
