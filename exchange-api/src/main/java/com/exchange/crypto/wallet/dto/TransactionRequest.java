package com.exchange.crypto.wallet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull(message = "O valor da transação é obrigatório.")
        @DecimalMin(value = "0.01", message = "O valor da transação deve ser maior que zero.")
        BigDecimal amount
) {}