package com.fiap.workshop.management.application.usecase.catalog;

import com.fiap.workshop.management.application.dto.catalog.ServiceCatalogItemResponse;
import com.fiap.workshop.management.application.dto.catalog.UpsertServiceCatalogItemCommand;
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

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateServiceCatalogItemUseCase")
class UpdateServiceCatalogItemUseCaseTest {

    @Mock
    private ServiceCatalogRepository serviceCatalogRepository;

    private UpdateServiceCatalogItemUseCase useCase;
    private ServiceCatalogItem item;
    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        useCase = new UpdateServiceCatalogItemUseCase(serviceCatalogRepository);
        item = new ServiceCatalogItem(id, "Oil Change", "Old desc", ServiceType.MECHANICAL, Money.of(150.00), true);
    }

    @Test
    @DisplayName("should update catalog item and return updated response")
    void shouldUpdateCatalogItemAndReturnUpdatedResponse() {
        UpsertServiceCatalogItemCommand command = new UpsertServiceCatalogItemCommand(
                "Wheel Alignment", "Updated desc", ServiceType.MECHANICAL, new BigDecimal("200.00"));
        when(serviceCatalogRepository.findById(id)).thenReturn(Optional.of(item));
        when(serviceCatalogRepository.save(item)).thenReturn(item);

        ServiceCatalogItemResponse response = useCase.execute(id, command);

        assertEquals("Wheel Alignment", item.getName());
        verify(serviceCatalogRepository).save(item);
        assertNotNull(response);
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when catalog item not found")
    void shouldThrowResourceNotFoundExceptionWhenCatalogItemNotFound() {
        UpsertServiceCatalogItemCommand command = new UpsertServiceCatalogItemCommand(
                "Wheel Alignment", "desc", ServiceType.MECHANICAL, new BigDecimal("200.00"));
        when(serviceCatalogRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(id, command));
        verify(serviceCatalogRepository, never()).save(any());
    }
}
