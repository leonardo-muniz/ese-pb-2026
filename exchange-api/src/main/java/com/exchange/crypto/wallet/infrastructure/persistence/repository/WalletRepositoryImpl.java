package com.exchange.crypto.wallet.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Repository;

import com.exchange.crypto.user.domain.entity.User;
import com.exchange.crypto.user.infrastructure.persistence.entity.UserJpaEntity;
import com.exchange.crypto.wallet.application.dto.WalletHistory;
import com.exchange.crypto.wallet.domain.entity.Wallet;
import com.exchange.crypto.wallet.domain.repository.WalletRepository;
import com.exchange.crypto.wallet.infrastructure.persistence.entity.WalletJpaEntity;

import jakarta.persistence.EntityManager;

@Repository
public class WalletRepositoryImpl implements WalletRepository {

    private final SpringDataWalletJpaRepository jpaRepository;
    private final EntityManager entityManager;

    public WalletRepositoryImpl(SpringDataWalletJpaRepository jpaRepository, EntityManager entityManager) {
        this.jpaRepository = jpaRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Wallet save(Wallet wallet) {
        WalletJpaEntity entity = toJpaEntity(wallet);
        WalletJpaEntity savedEntity = jpaRepository.save(entity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Wallet> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomainEntity);
    }

    @Override
    public List<Wallet> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<WalletHistory> findHistoryById(UUID id) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Object[]> results = auditReader.createQuery()
                .forRevisionsOfEntity(WalletJpaEntity.class, false, true)
                .add(AuditEntity.id().eq(id))
                .getResultList();

        return results.stream().map(result -> new WalletHistory(
                toDomainEntity((WalletJpaEntity) result[0]),
                ((DefaultRevisionEntity) result[1]).getId(),
                ((RevisionType) result[2]).name()
        )).toList();
    }

    // Mappers da Carteira
    private WalletJpaEntity toJpaEntity(Wallet wallet) {
        return WalletJpaEntity.builder()
                .id(wallet.getId())
                .user(toUserJpaEntity(wallet.getUser()))
                .currency(wallet.getCurrency())
                .balance(wallet.getBalance())
                .build();
    }

    private Wallet toDomainEntity(WalletJpaEntity entity) {
        return new Wallet(
                entity.getId(),
                toUserDomainEntity(entity.getUser()),
                entity.getCurrency(),
                entity.getBalance()
        );
    }

    // Mappers do Usuário embutido na Carteira
    private UserJpaEntity toUserJpaEntity(User user) {
        if (user == null) return null;
        return UserJpaEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private User toUserDomainEntity(UserJpaEntity entity) {
        if (entity == null) return null;
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getCreatedAt()
        );
    }
}