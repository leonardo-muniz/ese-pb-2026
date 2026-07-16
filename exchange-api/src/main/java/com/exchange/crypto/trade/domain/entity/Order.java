package com.exchange.crypto.trade.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.exchange.crypto.user.domain.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private UUID id;
    private User user;
    private OrderType type;
    private String cryptoCurrency;
    private BigDecimal amount;
    private BigDecimal price;
    private OrderStatus status;
    private LocalDateTime createdAt;

}