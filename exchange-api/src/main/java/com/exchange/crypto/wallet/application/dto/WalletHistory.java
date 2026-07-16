package com.exchange.crypto.wallet.application.dto;

import com.exchange.crypto.wallet.domain.entity.Wallet;

public record WalletHistory(Wallet wallet, Number revisionNumber, String revisionType) {}