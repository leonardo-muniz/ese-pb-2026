package com.exchange.crypto.wallet.application.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record TransactionRequest(

        @NotNull(message = "O valor da transação é obrigatório.")
        @DecimalMin(value = "0.01", message = "O valor da transação deve ser maior que zero.")
        BigDecimal amount

) {}