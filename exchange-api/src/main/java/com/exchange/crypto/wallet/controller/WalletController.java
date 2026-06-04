package com.exchange.crypto.wallet.controller;

import com.exchange.crypto.wallet.service.WalletService;
import com.exchange.crypto.wallet.model.Wallet;
import com.exchange.crypto.wallet.dto.TransactionRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) { this.walletService = walletService; }

    // CREATE
    @PostMapping("/{userId}")
    public ResponseEntity<Wallet> createWallet(@PathVariable UUID userId, @RequestParam String currency) {
        Wallet wallet = walletService.createWallet(userId, currency);
        return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
    }

    // READ
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
}