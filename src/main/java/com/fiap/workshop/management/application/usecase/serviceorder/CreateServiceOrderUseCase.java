package com.fiap.workshop.management.application.usecase.serviceorder;

import com.fiap.workshop.management.application.dto.serviceorder.CreateServiceOrderCommand;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.domain.exception.ResourceNotFoundException;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;
import com.fiap.workshop.management.domain.repository.CustomerRepository;
import com.fiap.workshop.management.domain.repository.ServiceOrderRepository;
import com.fiap.workshop.management.domain.repository.VehicleRepository;
import com.fiap.workshop.management.domain.service.ServiceOrderDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CreateServiceOrderUseCase {

    private final ServiceOrderRepository serviceOrderRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceOrderDomainService serviceOrderDomainService;

    public CreateServiceOrderUseCase(ServiceOrderRepository serviceOrderRepository,
                                      CustomerRepository customerRepository,
                                      VehicleRepository vehicleRepository,
                                      ServiceOrderDomainService serviceOrderDomainService) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
        this.serviceOrderDomainService = serviceOrderDomainService;
    }

    @Transactional
    public ServiceOrderResponse execute(CreateServiceOrderCommand command) {
        if (!customerRepository.findById(command.customerId()).isPresent()) {
            throw new ResourceNotFoundException("Customer", command.customerId());
        }
        if (!vehicleRepository.findById(command.vehicleId()).isPresent()) {
            throw new ResourceNotFoundException("Vehicle", command.vehicleId());
        }
        int year = LocalDateTime.now().getYear();
        long sequence = serviceOrderRepository.countByYear(year) + 1;
        String osCode = serviceOrderDomainService.generateOsCode(year, sequence);
        ServiceOrder order = ServiceOrder.create(osCode, command.customerId(), command.vehicleId(),
                command.customerComments());
        return ServiceOrderResponse.from(serviceOrderRepository.save(order));
    }
}
