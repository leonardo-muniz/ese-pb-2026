package com.exchange.crypto.wallet_service.application.dto;

import com.exchange.crypto.wallet_service.domain.entity.Wallet;

public record WalletHistory(Wallet wallet, Number revisionNumber, String revisionType) {}