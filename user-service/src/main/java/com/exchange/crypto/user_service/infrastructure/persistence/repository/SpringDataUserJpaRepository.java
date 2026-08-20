package com.exchange.crypto.user_service.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchange.crypto.user_service.infrastructure.persistence.entity.UserJpaEntity;

public interface SpringDataUserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmail(String email);
}