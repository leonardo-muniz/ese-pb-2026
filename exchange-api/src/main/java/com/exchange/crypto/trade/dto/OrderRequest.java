package com.exchange.crypto.trade.dto;

import com.exchange.crypto.trade.model.OrderType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderRequest(
        @NotNull(message = "O ID do usuário é obrigatório.") 
        UUID userId, 
        
        @NotNull(message = "O tipo de ordem (BUY/SELL) é obrigatório.") 
        OrderType type, 
        
        @NotBlank(message = "A sigla da criptomoeda é obrigatória.") 
        String cryptoCurrency, 
        
        @NotNull(message = "A quantidade é obrigatória.")
        @DecimalMin(value = "0.0001", message = "A quantidade mínima de operação é 0.0001.") 
        BigDecimal amount, 
        
        @NotNull(message = "O preço é obrigatório.")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.") 
        BigDecimal price
) {}