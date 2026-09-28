package com.exchange.crypto.wallet_service.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchange.crypto.wallet_service.infrastructure.persistence.entity.ProcessedEventJpaEntity;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEventJpaEntity, UUID> {
}
