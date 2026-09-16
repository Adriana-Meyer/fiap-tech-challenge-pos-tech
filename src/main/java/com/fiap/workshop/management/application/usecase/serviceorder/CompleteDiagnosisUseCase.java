package com.fiap.workshop.management.application.usecase.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.CompleteDiagnosisCommand;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.application.port.in.serviceorder.CompleteDiagnosisInputPort;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.customer.Customer;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;
import com.fiap.workshop.management.domain.repository.CustomerRepository;
import com.fiap.workshop.management.domain.repository.ServiceOrderRepository;
import com.fiap.workshop.management.domain.service.BudgetCalculationService;
import com.fiap.workshop.management.domain.service.EstimateNotificationService;
import com.fiap.workshop.management.domain.service.MetricsPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Service
public class CompleteDiagnosisUseCase implements CompleteDiagnosisInputPort {

    private final ServiceOrderRepository serviceOrderRepository;
    private final CustomerRepository customerRepository;
    private final BudgetCalculationService budgetCalculationService;
    private final EstimateNotificationService notificationService;
    private final MetricsPublisher metricsPublisher;

    public CompleteDiagnosisUseCase(ServiceOrderRepository serviceOrderRepository,
                                     CustomerRepository customerRepository,
                                     BudgetCalculationService budgetCalculationService,
                                     EstimateNotificationService notificationService,
                                     MetricsPublisher metricsPublisher) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.customerRepository = customerRepository;
        this.budgetCalculationService = budgetCalculationService;
        this.notificationService = notificationService;
        this.metricsPublisher = metricsPublisher;
    }

    @Transactional
    public ServiceOrderResponse execute(UUID id, CompleteDiagnosisCommand command) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder", id));
        order.completeDiagnosis(command.diagnosisNotes(), budgetCalculationService);
        ServiceOrder saved = serviceOrderRepository.save(order);
        metricsPublisher.recordServiceOrderStatusDuration(saved.getId(), "DIAGNOSIS",
                Duration.between(saved.getDiagnosisStartedAt(), saved.getWaitingApprovalAt()).toMinutes());
        Customer customer = customerRepository.findById(order.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", order.getCustomerId()));
        notificationService.notifyEstimateReady(saved, customer);
        return ServiceOrderResponse.from(saved);
    }
}
