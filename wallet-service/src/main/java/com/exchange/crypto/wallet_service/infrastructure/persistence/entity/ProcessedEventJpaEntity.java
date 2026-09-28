package com.exchange.crypto.wallet_service.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "processed_events")
public class ProcessedEventJpaEntity {

    @Id
    private UUID eventId;

    protected ProcessedEventJpaEntity() {}

    public ProcessedEventJpaEntity(UUID eventId) {
        this.eventId = eventId;
    }
}
