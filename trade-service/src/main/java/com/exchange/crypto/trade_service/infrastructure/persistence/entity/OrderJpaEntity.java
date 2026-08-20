package com.exchange.crypto.trade_service.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.envers.Audited;

import com.exchange.crypto.trade_service.domain.entity.OrderStatus;
import com.exchange.crypto.trade_service.domain.entity.OrderType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders") // "order" é reservado no PostgreSQL
@Audited // O Envers atua apenas na infraestrutura
public class OrderJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10, columnDefinition = "VARCHAR(10)")
    private OrderType type;

    @Column(nullable = false, length = 10)
    private String cryptoCurrency;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20)")
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public OrderJpaEntity() {}

    public OrderJpaEntity(UUID id, UUID userId, OrderType type, String cryptoCurrency, BigDecimal amount,
            BigDecimal price, OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.cryptoCurrency = cryptoCurrency;
        this.amount = amount;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public OrderType getType() { return type; }
    public String getCryptoCurrency() { return cryptoCurrency; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getPrice() { return price; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public void setType(OrderType type) { this.type = type; }
    public void setCryptoCurrency(String cryptoCurrency) { this.cryptoCurrency = cryptoCurrency; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}