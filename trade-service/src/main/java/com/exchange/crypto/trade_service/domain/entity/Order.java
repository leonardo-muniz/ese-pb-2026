package com.exchange.crypto.trade_service.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Order {

    private UUID id;
    private UUID userId;
    private OrderType type;
    private String cryptoCurrency;
    private BigDecimal amount;
    private BigDecimal price;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public Order() {}

    public Order(UUID id, UUID userId, OrderType type, String cryptoCurrency, BigDecimal amount,
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

}