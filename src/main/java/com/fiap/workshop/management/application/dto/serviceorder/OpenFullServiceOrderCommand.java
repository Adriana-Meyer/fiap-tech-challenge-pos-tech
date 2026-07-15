package com.fiap.workshop.management.application.dto.serviceorder;

import com.fiap.workshop.management.application.dto.customer.CreateCustomerCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OpenFullServiceOrderCommand(
        @Valid @NotNull CreateCustomerCommand customer,
        @Valid @NotNull VehicleIntakeCommand vehicle,
        String customerComments,
        @Valid List<AddItemCommand> items
) {}
