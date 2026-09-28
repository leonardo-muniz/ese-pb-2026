package com.exchange.crypto.trade_service.infrastructure.messaging;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class WalletEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public WalletEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void requestDebit(UUID orderId, UUID userId, String currency, java.math.BigDecimal amount) {
        WalletDebitRequested event = new WalletDebitRequested(
                UUID.randomUUID(), orderId, userId, currency, amount);
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE,
                RabbitMqConfig.WALLET_DEBIT_ROUTING_KEY,
                event);
    }
}
