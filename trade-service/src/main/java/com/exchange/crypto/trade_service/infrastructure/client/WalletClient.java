package com.exchange.crypto.trade_service.infrastructure.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "wallet-service")
public interface WalletClient {

    @GetMapping("/api/v1/wallets/user/{userId}")
    List<WalletResponse> getUserWallets(@PathVariable UUID userId);

    @PostMapping("/api/v1/wallets/{walletId}/withdraw")
    WalletResponse withdraw(@PathVariable UUID walletId, @RequestBody WalletTransactionRequest request);
}