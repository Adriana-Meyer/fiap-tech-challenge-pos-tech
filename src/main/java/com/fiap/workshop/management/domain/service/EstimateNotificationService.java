package com.fiap.workshop.management.domain.service;

import com.fiap.workshop.management.domain.model.customer.Customer;
import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrder;

public interface EstimateNotificationService {
    void notifyEstimateReady(ServiceOrder order, Customer customer);
}
