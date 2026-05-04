package com.fiap.workshop.management.infrastructure.notification;

import com.fiap.workshop.management.domain.service.EstimateNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MockEstimateNotificationService implements EstimateNotificationService {

    private static final Logger log = LoggerFactory.getLogger(MockEstimateNotificationService.class);

    public void notifyEstimateReady(String osCode, String customerContact) {
        log.info("[MOCK NOTIFICATION] Estimate ready for OS {} — contact: {}", osCode, customerContact);
    }
}
