package com.exchange.crypto.wallet.application.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.exchange.crypto.user.domain.entity.User;
import com.exchange.crypto.user.domain.repository.UserRepository;
import com.exchange.crypto.wallet.application.dto.WalletHistory;
import com.exchange.crypto.wallet.domain.entity.Wallet;
import com.exchange.crypto.wallet.domain.repository.WalletRepository;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    public Wallet createWallet(UUID userId, String currency) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Wallet newWallet = new Wallet(
                null,
                user,
                currency,
                BigDecimal.ZERO // Toda carteira nasce zerada
        );

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

        return walletRepository.save(wallet);
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

        return walletRepository.save(wallet);
    }

    public List<WalletHistory> getWalletHistory(UUID id) {
        List<WalletHistory> domainHistoryList = walletRepository.findHistoryById(id);

        return domainHistoryList.stream()
                .map(h -> new WalletHistory(
                        h.wallet(),
                        h.revisionNumber(),
                        h.revisionType()
                ))
                .toList();
    }
}