package com.fiap.workshop.management.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ServiceOrderDomainService")
class ServiceOrderDomainServiceTest {

    private final ServiceOrderDomainService service = new ServiceOrderDomainService();

    @Test
    @DisplayName("should generate OS code with correct prefix and year")
    void shouldGenerateOsCodeWithCorrectPrefixAndYear() {
        String code = service.generateOsCode(2026, 1);
        assertTrue(code.startsWith("OS-2026-"));
    }

    @Test
    @DisplayName("should pad sequence with leading zeros to 5 digits")
    void shouldPadSequenceWithLeadingZerosTo5Digits() {
        assertEquals("OS-2026-00001", service.generateOsCode(2026, 1));
        assertEquals("OS-2026-00042", service.generateOsCode(2026, 42));
        assertEquals("OS-2026-00100", service.generateOsCode(2026, 100));
    }

    @Test
    @DisplayName("should not pad when sequence has 5 or more digits")
    void shouldNotPadWhenSequenceHas5OrMoreDigits() {
        assertEquals("OS-2026-99999", service.generateOsCode(2026, 99999));
        assertEquals("OS-2026-100000", service.generateOsCode(2026, 100000));
    }

    @Test
    @DisplayName("should use the provided year in the generated code")
    void shouldUseTheProvidedYearInTheGeneratedCode() {
        assertEquals("OS-2025-00001", service.generateOsCode(2025, 1));
        assertEquals("OS-2027-00001", service.generateOsCode(2027, 1));
    }
}
