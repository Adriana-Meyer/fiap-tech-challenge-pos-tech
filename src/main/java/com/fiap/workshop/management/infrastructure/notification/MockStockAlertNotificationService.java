package com.fiap.workshop.management.infrastructure.notification;

import com.fiap.workshop.management.domain.service.StockAlertNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MockStockAlertNotificationService implements StockAlertNotificationService {

    private static final Logger log = LoggerFactory.getLogger(MockStockAlertNotificationService.class);

    @Override
    public void notifyNegativeStock(String supplyCode, String supplyName, int currentStock) {
        log.warn("[MOCK STOCK ALERT] Supply {} ({}) is now at {} units — restock required",
                supplyCode, supplyName, currentStock);
    }
}
