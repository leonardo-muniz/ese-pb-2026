package com.exchange.crypto.wallet_service.infrastructure.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.exchange.crypto.wallet_service.application.service.WalletService;
import com.exchange.crypto.wallet_service.infrastructure.persistence.entity.ProcessedEventJpaEntity;
import com.exchange.crypto.wallet_service.infrastructure.persistence.repository.ProcessedEventRepository;

@Component
public class WalletEventListener {

    private final WalletService walletService;
    private final ProcessedEventRepository processedEventRepository;

    public WalletEventListener(WalletService walletService, ProcessedEventRepository processedEventRepository) {
        this.walletService = walletService;
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
    @RabbitListener(queues = RabbitMqConfig.WALLET_DEBIT_QUEUE)
    public void handleDebit(WalletDebitRequested event) {
        if (processedEventRepository.existsById(event.eventId())) {
            return;
        }

        walletService.withdrawByUserAndCurrency(event.userId(), event.currency(), event.amount());
        processedEventRepository.save(new ProcessedEventJpaEntity(event.eventId()));
    }
}
