package com.exchange.crypto.wallet.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exchange.crypto.wallet.application.dto.TransactionRequest;
import com.exchange.crypto.wallet.application.dto.WalletHistory;
import com.exchange.crypto.wallet.application.service.WalletService;
import com.exchange.crypto.wallet.domain.entity.Wallet;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) { this.walletService = walletService; }

    @PostMapping("/{userId}")
    public ResponseEntity<Wallet> createWallet(@PathVariable UUID userId, @RequestParam String currency) {
        Wallet wallet = walletService.createWallet(userId, currency);
        return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Wallet>> getUserWallets(@PathVariable UUID userId) {
        return ResponseEntity.ok(walletService.getWalletsByUser(userId));
    }

    // DEPOSIT
    @PostMapping("/{walletId}/deposit")
    public ResponseEntity<Wallet> deposit(@PathVariable UUID walletId, @Valid @RequestBody TransactionRequest request) {
        Wallet updatedWallet = walletService.deposit(walletId, request.amount());
        return ResponseEntity.ok(updatedWallet);
    }

    // WITHDRAW
    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<Wallet> withdraw(@PathVariable UUID walletId, @Valid @RequestBody TransactionRequest request) {
        Wallet updatedWallet = walletService.withdraw(walletId, request.amount());
        return ResponseEntity.ok(updatedWallet);
    }

    // Buscar o histórico de auditoria do Hibernate Envers
    @GetMapping("/{id}/history")
    public ResponseEntity<List<WalletHistory>> getWalletHistory(@PathVariable UUID id) {
        return ResponseEntity.ok(walletService.getWalletHistory(id));
    }
}