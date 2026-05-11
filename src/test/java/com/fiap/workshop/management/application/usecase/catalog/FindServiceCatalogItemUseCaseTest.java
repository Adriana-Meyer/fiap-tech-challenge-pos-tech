package com.fiap.workshop.management.application.usecase.catalog;

import com.fiap.workshop.management.application.dto.catalog.ServiceCatalogItemResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.catalog.ServiceCatalogItem;
import com.fiap.workshop.management.domain.model.catalog.ServiceType;
import com.fiap.workshop.management.domain.model.shared.Money;
import com.fiap.workshop.management.domain.repository.ServiceCatalogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindServiceCatalogItemUseCase")
class FindServiceCatalogItemUseCaseTest {

    @Mock
    private ServiceCatalogRepository serviceCatalogRepository;

    private FindServiceCatalogItemUseCase useCase;
    private ServiceCatalogItem item;
    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        useCase = new FindServiceCatalogItemUseCase(serviceCatalogRepository);
        item = new ServiceCatalogItem(id, "Oil Change", "Engine oil change", ServiceType.MECHANICAL, Money.of(150.00), true);
    }

    @Test
    @DisplayName("should return catalog item response when found by id")
    void shouldReturnCatalogItemResponseWhenFoundById() {
        when(serviceCatalogRepository.findById(id)).thenReturn(Optional.of(item));

        ServiceCatalogItemResponse response = useCase.findById(id);

        assertEquals(id, response.id());
        assertEquals("Oil Change", response.name());
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when catalog item not found by id")
    void shouldThrowResourceNotFoundExceptionWhenCatalogItemNotFoundById() {
        when(serviceCatalogRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.findById(id));
    }

    @Test
    @DisplayName("should return all catalog items as response list")
    void shouldReturnAllCatalogItemsAsResponseList() {
        when(serviceCatalogRepository.findAll()).thenReturn(List.of(item));

        List<ServiceCatalogItemResponse> result = useCase.findAll();

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).id());
    }

    @Test
    @DisplayName("should return only active catalog items")
    void shouldReturnOnlyActiveCatalogItems() {
        when(serviceCatalogRepository.findAllActive()).thenReturn(List.of(item));

        List<ServiceCatalogItemResponse> result = useCase.findAllActive();

        assertEquals(1, result.size());
        assertTrue(result.get(0).active());
    }
}
