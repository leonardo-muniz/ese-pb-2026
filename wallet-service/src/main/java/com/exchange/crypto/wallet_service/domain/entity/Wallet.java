package com.exchange.crypto.wallet_service.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;

public class Wallet {

    private UUID id;
    private UUID userId;
    private String currency;
    private BigDecimal balance;

    public Wallet() {}

    public Wallet(UUID id, UUID userId, String currency, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.currency = currency;
        this.balance = balance;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getCurrency() { return currency; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

}