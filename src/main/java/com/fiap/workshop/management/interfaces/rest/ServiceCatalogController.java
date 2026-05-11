package com.fiap.workshop.management.interfaces.rest;

import com.fiap.workshop.management.application.dto.catalog.ServiceCatalogItemResponse;
import com.fiap.workshop.management.application.dto.catalog.UpsertServiceCatalogItemCommand;
import com.fiap.workshop.management.application.usecase.catalog.CreateServiceCatalogItemUseCase;
import com.fiap.workshop.management.application.usecase.catalog.DeleteServiceCatalogItemUseCase;
import com.fiap.workshop.management.application.usecase.catalog.FindServiceCatalogItemUseCase;
import com.fiap.workshop.management.application.usecase.catalog.UpdateServiceCatalogItemUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceCatalogController {

    private final CreateServiceCatalogItemUseCase createUseCase;
    private final FindServiceCatalogItemUseCase findUseCase;
    private final UpdateServiceCatalogItemUseCase updateUseCase;
    private final DeleteServiceCatalogItemUseCase deleteUseCase;

    public ServiceCatalogController(CreateServiceCatalogItemUseCase createUseCase,
                                     FindServiceCatalogItemUseCase findUseCase,
                                     UpdateServiceCatalogItemUseCase updateUseCase,
                                     DeleteServiceCatalogItemUseCase deleteUseCase) {
        this.createUseCase = createUseCase;
        this.findUseCase = findUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceCatalogItemResponse create(@Valid @RequestBody UpsertServiceCatalogItemCommand command) {
        return createUseCase.execute(command);
    }

    @GetMapping
    public List<ServiceCatalogItemResponse> findAll(
            @RequestParam(defaultValue = "false") boolean includeInactive) {
        return includeInactive ? findUseCase.findAll() : findUseCase.findAllActive();
    }

    @GetMapping("/{id}")
    public ServiceCatalogItemResponse findById(@PathVariable UUID id) {
        return findUseCase.findById(id);
    }

    @PutMapping("/{id}")
    public ServiceCatalogItemResponse update(@PathVariable UUID id,
                                              @Valid @RequestBody UpsertServiceCatalogItemCommand command) {
        return updateUseCase.execute(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteUseCase.execute(id);
    }
}
