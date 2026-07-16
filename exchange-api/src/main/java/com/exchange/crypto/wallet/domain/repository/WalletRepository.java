package com.exchange.crypto.wallet.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.exchange.crypto.wallet.application.dto.WalletHistory;
import com.exchange.crypto.wallet.domain.entity.Wallet;

public interface WalletRepository {
    Wallet save(Wallet wallet);
    Optional<Wallet> findById(UUID id);
    List<Wallet> findByUserId(UUID userId);
    List<WalletHistory> findHistoryById(UUID id);
}