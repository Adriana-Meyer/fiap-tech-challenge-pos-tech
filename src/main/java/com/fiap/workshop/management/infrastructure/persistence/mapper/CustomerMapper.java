package com.fiap.workshop.management.infrastructure.persistence.mapper;

import com.fiap.workshop.management.domain.model.customer.Customer;
import com.fiap.workshop.management.domain.model.customer.Document;
import com.fiap.workshop.management.infrastructure.persistence.entity.CustomerJpaEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerMapper {

    public Customer toDomain(CustomerJpaEntity e) {
        return new Customer(
                UUID.fromString(e.getId()),
                e.getName(),
                new Document(e.getDocumentValue()),
                e.getPhone(),
                e.getEmail(),
                e.getCreatedAt()
        );
    }

    public CustomerJpaEntity toEntity(Customer c) {
        return new CustomerJpaEntity(
                c.getId().toString(),
                c.getDocument().getValue(),
                c.getDocument().getType().name(),
                c.getName(),
                c.getPhone(),
                c.getEmail(),
                c.getCreatedAt()
        );
    }
}
