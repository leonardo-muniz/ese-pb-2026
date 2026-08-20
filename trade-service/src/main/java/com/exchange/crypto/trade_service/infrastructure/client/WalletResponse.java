package com.exchange.crypto.trade_service.infrastructure.client;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(UUID id, UUID userId, String currency, BigDecimal balance) {}