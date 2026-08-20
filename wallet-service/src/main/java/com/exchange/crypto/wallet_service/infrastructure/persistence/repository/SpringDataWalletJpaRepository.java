package com.exchange.crypto.wallet_service.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchange.crypto.wallet_service.infrastructure.persistence.entity.WalletJpaEntity;

public interface SpringDataWalletJpaRepository extends JpaRepository<WalletJpaEntity, UUID> {
    List<WalletJpaEntity> findByUserId(UUID userId);
}