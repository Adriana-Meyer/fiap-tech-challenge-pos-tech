package com.fiap.workshop.management.interfaces.rest;

import com.fiap.workshop.management.application.dto.customer.CreateCustomerCommand;
import com.fiap.workshop.management.application.dto.customer.CustomerResponse;
import com.fiap.workshop.management.application.dto.customer.UpdateCustomerCommand;
import com.fiap.workshop.management.application.usecase.customer.CreateCustomerUseCase;
import com.fiap.workshop.management.application.usecase.customer.DeleteCustomerUseCase;
import com.fiap.workshop.management.application.usecase.customer.FindCustomerUseCase;
import com.fiap.workshop.management.application.usecase.customer.UpdateCustomerUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final FindCustomerUseCase findCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;

    public CustomerController(CreateCustomerUseCase createCustomerUseCase,
                               FindCustomerUseCase findCustomerUseCase,
                               UpdateCustomerUseCase updateCustomerUseCase,
                               DeleteCustomerUseCase deleteCustomerUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.findCustomerUseCase = findCustomerUseCase;
        this.updateCustomerUseCase = updateCustomerUseCase;
        this.deleteCustomerUseCase = deleteCustomerUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(@Valid @RequestBody CreateCustomerCommand command) {
        return createCustomerUseCase.execute(command);
    }

    @GetMapping
    public List<CustomerResponse> findAll() {
        return findCustomerUseCase.findAll();
    }

    @GetMapping("/{id}")
    public CustomerResponse findById(@PathVariable UUID id) {
        return findCustomerUseCase.findById(id);
    }

    @GetMapping("/document/{document}")
    public CustomerResponse findByDocument(@PathVariable String document) {
        return findCustomerUseCase.findByDocument(document);
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateCustomerCommand command) {
        return updateCustomerUseCase.execute(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteCustomerUseCase.execute(id);
    }
}
