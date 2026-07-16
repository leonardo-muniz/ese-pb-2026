package com.exchange.crypto.trade.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchange.crypto.trade.infrastructure.persistence.entity.OrderJpaEntity;

public interface SpringDataOrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {
    List<OrderJpaEntity> findByUserId(UUID userId);
}