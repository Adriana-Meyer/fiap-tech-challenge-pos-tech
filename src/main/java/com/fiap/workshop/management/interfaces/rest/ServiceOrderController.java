package com.fiap.workshop.management.interfaces.rest;

import com.fiap.workshop.management.application.dto.serviceorder.AddItemCommand;
import com.fiap.workshop.management.application.dto.serviceorder.AverageExecutionTimeResponse;
import com.fiap.workshop.management.application.dto.serviceorder.CompleteDiagnosisCommand;
import com.fiap.workshop.management.application.dto.serviceorder.CreateServiceOrderCommand;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderSummaryResponse;
import com.fiap.workshop.management.application.usecase.serviceorder.AddItemToServiceOrderUseCase;
import com.fiap.workshop.management.application.usecase.serviceorder.ApproveEstimateUseCase;
import com.fiap.workshop.management.application.usecase.serviceorder.CompleteDiagnosisUseCase;
import com.fiap.workshop.management.application.usecase.serviceorder.CreateServiceOrderUseCase;
import com.fiap.workshop.management.application.usecase.serviceorder.DeliverServiceOrderUseCase;
import com.fiap.workshop.management.application.usecase.serviceorder.FinishServiceItemExecutionUseCase;
import com.fiap.workshop.management.application.usecase.serviceorder.FindServiceOrderUseCase;
import com.fiap.workshop.management.application.usecase.serviceorder.GetAverageExecutionTimeUseCase;
import com.fiap.workshop.management.application.usecase.serviceorder.RejectEstimateUseCase;
import com.fiap.workshop.management.application.usecase.serviceorder.RemoveItemFromServiceOrderUseCase;
import com.fiap.workshop.management.application.usecase.serviceorder.StartDiagnosisUseCase;
import com.fiap.workshop.management.application.usecase.serviceorder.StartServiceItemExecutionUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/service-orders")
public class ServiceOrderController {

    private final CreateServiceOrderUseCase createUseCase;
    private final FindServiceOrderUseCase findUseCase;
    private final StartDiagnosisUseCase startDiagnosisUseCase;
    private final AddItemToServiceOrderUseCase addItemUseCase;
    private final RemoveItemFromServiceOrderUseCase removeItemUseCase;
    private final CompleteDiagnosisUseCase completeDiagnosisUseCase;
    private final ApproveEstimateUseCase approveEstimateUseCase;
    private final RejectEstimateUseCase rejectEstimateUseCase;
    private final StartServiceItemExecutionUseCase startItemUseCase;
    private final FinishServiceItemExecutionUseCase finishItemUseCase;
    private final DeliverServiceOrderUseCase deliverUseCase;
    private final GetAverageExecutionTimeUseCase avgExecutionTimeUseCase;

    public ServiceOrderController(CreateServiceOrderUseCase createUseCase,
                                   FindServiceOrderUseCase findUseCase,
                                   StartDiagnosisUseCase startDiagnosisUseCase,
                                   AddItemToServiceOrderUseCase addItemUseCase,
                                   RemoveItemFromServiceOrderUseCase removeItemUseCase,
                                   CompleteDiagnosisUseCase completeDiagnosisUseCase,
                                   ApproveEstimateUseCase approveEstimateUseCase,
                                   RejectEstimateUseCase rejectEstimateUseCase,
                                   StartServiceItemExecutionUseCase startItemUseCase,
                                   FinishServiceItemExecutionUseCase finishItemUseCase,
                                   DeliverServiceOrderUseCase deliverUseCase,
                                   GetAverageExecutionTimeUseCase avgExecutionTimeUseCase) {
        this.createUseCase = createUseCase;
        this.findUseCase = findUseCase;
        this.startDiagnosisUseCase = startDiagnosisUseCase;
        this.addItemUseCase = addItemUseCase;
        this.removeItemUseCase = removeItemUseCase;
        this.completeDiagnosisUseCase = completeDiagnosisUseCase;
        this.approveEstimateUseCase = approveEstimateUseCase;
        this.rejectEstimateUseCase = rejectEstimateUseCase;
        this.startItemUseCase = startItemUseCase;
        this.finishItemUseCase = finishItemUseCase;
        this.deliverUseCase = deliverUseCase;
        this.avgExecutionTimeUseCase = avgExecutionTimeUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceOrderResponse create(@Valid @RequestBody CreateServiceOrderCommand command) {
        return createUseCase.execute(command);
    }

    @GetMapping
    public List<ServiceOrderSummaryResponse> findAll() {
        return findUseCase.findAll();
    }

    @GetMapping("/{id}")
    public ServiceOrderResponse findById(@PathVariable UUID id) {
        return findUseCase.findById(id);
    }

    @GetMapping("/analytics/avg-execution-time")
    public List<AverageExecutionTimeResponse> avgExecutionTime() {
        return avgExecutionTimeUseCase.execute();
    }

    @PostMapping("/{id}/items")
    public ServiceOrderResponse addItem(@PathVariable UUID id,
                                         @Valid @RequestBody AddItemCommand command) {
        return addItemUseCase.execute(id, command);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ServiceOrderResponse removeItem(@PathVariable UUID id, @PathVariable UUID itemId) {
        return removeItemUseCase.execute(id, itemId);
    }

    @PatchMapping("/{id}/diagnosis/start")
    public ServiceOrderResponse startDiagnosis(@PathVariable UUID id) {
        return startDiagnosisUseCase.execute(id);
    }

    @PostMapping("/{id}/diagnosis/complete")
    public ServiceOrderResponse completeDiagnosis(@PathVariable UUID id,
                                                   @Valid @RequestBody CompleteDiagnosisCommand command) {
        return completeDiagnosisUseCase.execute(id, command);
    }

    @PatchMapping("/{id}/estimate/approve")
    public ServiceOrderResponse approveEstimate(@PathVariable UUID id) {
        return approveEstimateUseCase.execute(id);
    }

    @PatchMapping("/{id}/estimate/reject")
    public ServiceOrderResponse rejectEstimate(@PathVariable UUID id) {
        return rejectEstimateUseCase.execute(id);
    }

    @PatchMapping("/{id}/items/{itemId}/start")
    public ServiceOrderResponse startItemExecution(@PathVariable UUID id, @PathVariable UUID itemId) {
        return startItemUseCase.execute(id, itemId);
    }

    @PatchMapping("/{id}/items/{itemId}/finish")
    public ServiceOrderResponse finishItemExecution(@PathVariable UUID id, @PathVariable UUID itemId) {
        return finishItemUseCase.execute(id, itemId);
    }

    @PatchMapping("/{id}/deliver")
    public ServiceOrderResponse deliver(@PathVariable UUID id) {
        return deliverUseCase.execute(id);
    }
}
