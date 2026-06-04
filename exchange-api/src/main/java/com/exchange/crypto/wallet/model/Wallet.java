package com.exchange.crypto.wallet.model;

import lombok.*;
import java.util.UUID;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    private UUID id;
    private UUID userId;
    private String currency;
    private BigDecimal balance;
}