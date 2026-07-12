package com.fiap.workshop.management.infrastructure.security;

import com.fiap.workshop.management.domain.exception.InvalidWebhookTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebhookTokenValidator {

    @Value("${app.webhook.token}")
    private String expectedToken;

    public void validate(String providedToken) {
        if (providedToken == null || !providedToken.equals(expectedToken)) {
            throw new InvalidWebhookTokenException("Invalid or missing webhook token");
        }
    }
}
