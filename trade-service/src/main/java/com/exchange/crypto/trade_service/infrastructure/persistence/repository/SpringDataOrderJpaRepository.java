package com.exchange.crypto.trade_service.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchange.crypto.trade_service.infrastructure.persistence.entity.OrderJpaEntity;

public interface SpringDataOrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {
    List<OrderJpaEntity> findByUserId(UUID userId);
}