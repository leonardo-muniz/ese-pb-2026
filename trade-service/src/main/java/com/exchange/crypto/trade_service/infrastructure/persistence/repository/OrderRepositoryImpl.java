package com.exchange.crypto.trade_service.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Repository;

import com.exchange.crypto.trade_service.application.dto.OrderHistory;
import com.exchange.crypto.trade_service.domain.entity.Order;
import com.exchange.crypto.trade_service.domain.repository.OrderRepository;
import com.exchange.crypto.trade_service.infrastructure.persistence.entity.OrderJpaEntity;

import jakarta.persistence.EntityManager;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final SpringDataOrderJpaRepository jpaRepository;
    private final EntityManager entityManager;

    public OrderRepositoryImpl(SpringDataOrderJpaRepository jpaRepository, EntityManager entityManager) {
        this.jpaRepository = jpaRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = toJpaEntity(order);
        OrderJpaEntity savedEntity = jpaRepository.save(entity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomainEntity);
    }

    @Override
    public List<Order> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OrderHistory> findHistoryById(UUID id) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Object[]> results = auditReader.createQuery()
                .forRevisionsOfEntity(OrderJpaEntity.class, false, true)
                .add(AuditEntity.id().eq(id))
                .getResultList();

        return results.stream().map(result -> new OrderHistory(
                toDomainEntity((OrderJpaEntity) result[0]),
                ((DefaultRevisionEntity) result[1]).getId(),
                ((RevisionType) result[2]).name()
        )).toList();
    }

    // Mappers da Ordem
    private OrderJpaEntity toJpaEntity(Order order) {
        return new OrderJpaEntity(
            order.getId(),
            order.getUserId(),
            order.getType(),
            order.getCryptoCurrency(),
            order.getAmount(),
            order.getPrice(),
            order.getStatus(),
            order.getCreatedAt()
        );
    }

    private Order toDomainEntity(OrderJpaEntity entity) {
        return new Order(
                entity.getId(),
                entity.getUserId(),
                entity.getType(),
                entity.getCryptoCurrency(),
                entity.getAmount(),
                entity.getPrice(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

}