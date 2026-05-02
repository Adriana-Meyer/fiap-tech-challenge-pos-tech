package com.fiap.workshop.management.infrastructure.config;

import com.fiap.workshop.management.domain.repository.SupplyRepository;
import com.fiap.workshop.management.domain.service.BudgetCalculationService;
import com.fiap.workshop.management.domain.service.ServiceOrderDomainService;
import com.fiap.workshop.management.domain.service.StockManagementService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public BudgetCalculationService budgetCalculationService() {
        return new BudgetCalculationService();
    }

    @Bean
    public ServiceOrderDomainService serviceOrderDomainService() {
        return new ServiceOrderDomainService();
    }

    @Bean
    public StockManagementService stockManagementService(SupplyRepository supplyRepository) {
        return new StockManagementService(supplyRepository);
    }
}
