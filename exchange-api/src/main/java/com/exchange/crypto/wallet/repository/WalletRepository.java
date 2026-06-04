package com.exchange.crypto.wallet.repository;

import com.exchange.crypto.wallet.model.Wallet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class WalletRepository {
    
    private final List<Wallet> wallets = new ArrayList<>();

    public Wallet save(Wallet wallet) {
        if (wallet.getId() == null) {
            wallet.setId(UUID.randomUUID());
        } else {
            wallets.removeIf(w -> w.getId().equals(wallet.getId()));
        }
        wallets.add(wallet);
        return wallet;
    }

    public List<Wallet> findByUserId(UUID userId) {
        return wallets.stream()
                .filter(w -> w.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Optional<Wallet> findById(UUID id) {
        return wallets.stream()
                .filter(w -> w.getId().equals(id))
                .findFirst();
    }
}