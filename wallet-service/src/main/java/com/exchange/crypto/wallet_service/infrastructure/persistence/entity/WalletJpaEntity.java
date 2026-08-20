package com.exchange.crypto.wallet_service.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "wallets")
@Audited // O Envers atua apenas na infraestrutura
public class WalletJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal balance;

    public WalletJpaEntity() {}

    public WalletJpaEntity(UUID id, UUID userId, String currency, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.currency = currency;
        this.balance = balance;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getCurrency() { return currency; }
    public BigDecimal getBalance() { return balance; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public void setCurrency(String currency) { this.currency = currency; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

}