package com.exchange.crypto.trade_service.infrastructure.messaging;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDebitRequested(
        UUID eventId,
        UUID orderId,
        UUID userId,
        String currency,
        BigDecimal amount
) {}
