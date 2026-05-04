package com.fiap.workshop.management.domain.service;

public interface StockAlertNotificationService {
    void notifyNegativeStock(String supplyCode, String supplyName, int currentStock);
}
