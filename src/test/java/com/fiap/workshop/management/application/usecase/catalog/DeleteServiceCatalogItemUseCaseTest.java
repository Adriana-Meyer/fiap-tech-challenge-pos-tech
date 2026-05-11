package com.fiap.workshop.management.application.usecase.catalog;

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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteServiceCatalogItemUseCase")
class DeleteServiceCatalogItemUseCaseTest {

    @Mock
    private ServiceCatalogRepository serviceCatalogRepository;

    private DeleteServiceCatalogItemUseCase useCase;
    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        useCase = new DeleteServiceCatalogItemUseCase(serviceCatalogRepository);
    }

    @Test
    @DisplayName("should deactivate catalog item on soft delete")
    void shouldDeactivateCatalogItemOnSoftDelete() {
        ServiceCatalogItem item = new ServiceCatalogItem(id, "Oil Change", "desc", ServiceType.MECHANICAL, Money.of(150.00), true);
        when(serviceCatalogRepository.findById(id)).thenReturn(Optional.of(item));

        assertDoesNotThrow(() -> useCase.execute(id));

        assertFalse(item.isActive());
        verify(serviceCatalogRepository).save(item);
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when catalog item not found")
    void shouldThrowResourceNotFoundExceptionWhenCatalogItemNotFound() {
        when(serviceCatalogRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(id));
        verify(serviceCatalogRepository, never()).save(any());
    }
}
