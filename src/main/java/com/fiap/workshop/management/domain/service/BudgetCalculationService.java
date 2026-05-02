package com.fiap.workshop.management.domain.service;

import com.fiap.workshop.management.domain.model.serviceorder.ServiceOrderItem;
import com.fiap.workshop.management.domain.model.shared.Money;

import java.util.List;

public class BudgetCalculationService {

    public Money calculate(List<ServiceOrderItem> items) {
        return items.stream()
                .map(ServiceOrderItem::getSubtotal)
                .reduce(Money.zero(), Money::add);
    }
}
