package com.fiap.workshop.management.interfaces.rest;

import com.fiap.workshop.management.application.dto.serviceorder.AddItemCommand;
import com.fiap.workshop.management.application.dto.serviceorder.AverageExecutionTimeResponse;
import com.fiap.workshop.management.application.dto.serviceorder.CompleteDiagnosisCommand;
import com.fiap.workshop.management.application.dto.serviceorder.CreateServiceOrderCommand;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderSummaryResponse;
import com.fiap.workshop.management.application.port.in.serviceorder.AddItemToServiceOrderInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.ApproveEstimateInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.CompleteDiagnosisInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.CreateServiceOrderInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.DeliverServiceOrderInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.FindServiceOrderInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.FinishServiceItemExecutionInputPort;
import com.fiap.workshop.management.application.dto.serviceorder.OpenFullServiceOrderCommand;
import com.fiap.workshop.management.application.port.in.serviceorder.GetAverageExecutionTimeInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.OpenFullServiceOrderInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.RejectEstimateInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.RemoveItemFromServiceOrderInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.StartDiagnosisInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.StartServiceItemExecutionInputPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/service-orders")
public class ServiceOrderController {

    private final CreateServiceOrderInputPort createUseCase;
    private final FindServiceOrderInputPort findUseCase;
    private final StartDiagnosisInputPort startDiagnosisUseCase;
    private final AddItemToServiceOrderInputPort addItemUseCase;
    private final RemoveItemFromServiceOrderInputPort removeItemUseCase;
    private final CompleteDiagnosisInputPort completeDiagnosisUseCase;
    private final ApproveEstimateInputPort approveEstimateUseCase;
    private final RejectEstimateInputPort rejectEstimateUseCase;
    private final StartServiceItemExecutionInputPort startItemUseCase;
    private final FinishServiceItemExecutionInputPort finishItemUseCase;
    private final DeliverServiceOrderInputPort deliverUseCase;
    private final GetAverageExecutionTimeInputPort avgExecutionTimeUseCase;
    private final OpenFullServiceOrderInputPort openFullUseCase;

    public ServiceOrderController(CreateServiceOrderInputPort createUseCase,
                                   FindServiceOrderInputPort findUseCase,
                                   StartDiagnosisInputPort startDiagnosisUseCase,
                                   AddItemToServiceOrderInputPort addItemUseCase,
                                   RemoveItemFromServiceOrderInputPort removeItemUseCase,
                                   CompleteDiagnosisInputPort completeDiagnosisUseCase,
                                   ApproveEstimateInputPort approveEstimateUseCase,
                                   RejectEstimateInputPort rejectEstimateUseCase,
                                   StartServiceItemExecutionInputPort startItemUseCase,
                                   FinishServiceItemExecutionInputPort finishItemUseCase,
                                   DeliverServiceOrderInputPort deliverUseCase,
                                   GetAverageExecutionTimeInputPort avgExecutionTimeUseCase,
                                   OpenFullServiceOrderInputPort openFullUseCase) {
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
        this.openFullUseCase = openFullUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceOrderResponse create(@Valid @RequestBody CreateServiceOrderCommand command) {
        return createUseCase.execute(command);
    }

    @PostMapping("/full")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceOrderResponse openFull(@Valid @RequestBody OpenFullServiceOrderCommand command) {
        return openFullUseCase.execute(command);
    }

    @GetMapping
    public List<ServiceOrderSummaryResponse> findAll(
            @RequestParam(defaultValue = "false") boolean includeCompleted) {
        return findUseCase.findAll(includeCompleted);
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
