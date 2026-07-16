package com.exchange.crypto.wallet.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.exchange.crypto.user.domain.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    private UUID id;
    private User user;
    private String currency;
    private BigDecimal balance;

}