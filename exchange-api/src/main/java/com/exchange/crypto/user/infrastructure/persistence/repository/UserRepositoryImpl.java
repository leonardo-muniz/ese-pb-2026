package com.exchange.crypto.user.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Repository;

import com.exchange.crypto.user.application.dto.UserHistory;
import com.exchange.crypto.user.domain.entity.User;
import com.exchange.crypto.user.domain.repository.UserRepository;
import com.exchange.crypto.user.infrastructure.persistence.entity.UserJpaEntity;

import jakarta.persistence.EntityManager;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SpringDataUserJpaRepository jpaRepository;
    private final EntityManager entityManager;

    public UserRepositoryImpl(SpringDataUserJpaRepository jpaRepository, EntityManager entityManager) {
        this.jpaRepository = jpaRepository;
        this.entityManager = entityManager;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = toJpaEntity(user);
        UserJpaEntity savedEntity = jpaRepository.save(entity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomainEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomainEntity);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public void delete(User user) {
        jpaRepository.delete(toJpaEntity(user));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UserHistory> findHistoryById(UUID id) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        List<Object[]> results = auditReader.createQuery()
                .forRevisionsOfEntity(UserJpaEntity.class, false, true)
                .add(AuditEntity.id().eq(id))
                .getResultList();

        return results.stream().map(result -> {
            UserJpaEntity entity = (UserJpaEntity) result[0];
            DefaultRevisionEntity revision = (DefaultRevisionEntity) result[1];
            RevisionType revType = (RevisionType) result[2];

            return new UserHistory(
                    toDomainEntity(entity),
                    revision.getId(),
                    revType.name() // Retorna "ADD", "MOD" ou "DEL"
            );
        }).toList();
    }

    // Mappers
    private UserJpaEntity toJpaEntity(User user) {
        return UserJpaEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private User toDomainEntity(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getCreatedAt()
        );
    }
}