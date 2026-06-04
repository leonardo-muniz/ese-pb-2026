package com.exchange.crypto.wallet.service;

import com.exchange.crypto.wallet.model.Wallet;
import com.exchange.crypto.wallet.repository.WalletRepository;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(UUID userId, String currency) {
        Wallet newWallet = Wallet.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .currency(currency)
                .balance(BigDecimal.ZERO) // Toda carteira nasce zerada
                .build();

        return walletRepository.save(newWallet);
    }

    public List<Wallet> getWalletsByUser(UUID userId) {
        return walletRepository.findByUserId(userId);
    }

    public Wallet deposit(UUID walletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser maior que zero.");
        }

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Carteira não encontrada no sistema."));

        wallet.setBalance(wallet.getBalance().add(amount));
        return wallet;
    }

    public Wallet withdraw(UUID walletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser maior que zero.");
        }

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Carteira não encontrada no sistema."));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Operação recusada: Saldo insuficiente.");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        return wallet;
    }
}