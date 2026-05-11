package com.fiap.workshop.management.domain.service;

public class ServiceOrderDomainService {

    public String generateOsCode(int year, long sequence) {
        return String.format("OS-%d-%05d", year, sequence);
    }
}
