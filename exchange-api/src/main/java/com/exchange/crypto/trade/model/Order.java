package com.exchange.crypto.trade.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private UUID id;
    private UUID userId;
    private OrderType type;
    private String cryptoCurrency;
    private BigDecimal amount;
    private BigDecimal price;
    private OrderStatus status;
    private LocalDateTime createdAt;
}