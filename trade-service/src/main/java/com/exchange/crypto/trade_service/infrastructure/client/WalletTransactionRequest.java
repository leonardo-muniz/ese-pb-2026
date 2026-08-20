package com.exchange.crypto.trade_service.infrastructure.client;

import java.math.BigDecimal;

public record WalletTransactionRequest(BigDecimal amount) {}