package com.exchange.crypto.trade_service.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.exchange.crypto.trade_service.application.dto.OrderHistory;
import com.exchange.crypto.trade_service.domain.entity.Order;
import com.exchange.crypto.trade_service.domain.entity.OrderStatus;
import com.exchange.crypto.trade_service.domain.entity.OrderType;
import com.exchange.crypto.trade_service.domain.repository.OrderRepository;
import com.exchange.crypto.trade_service.infrastructure.messaging.WalletEventPublisher;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WalletEventPublisher walletEventPublisher;

    public OrderService(OrderRepository orderRepository, WalletEventPublisher walletEventPublisher) {
        this.orderRepository = orderRepository;
        this.walletEventPublisher = walletEventPublisher;
    }

    public Order createOrder(UUID userId, OrderType type, String cryptoCurrency, BigDecimal amount, BigDecimal price) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0 || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade e o preço devem ser maiores que zero.");
        }

        BigDecimal totalCost = amount.multiply(price);

        Order newOrder = new Order(
                null,
                userId,
                type,
                cryptoCurrency,
                amount,
                price,
                OrderStatus.OPEN,
                LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
        );

        Order savedOrder = orderRepository.save(newOrder);

        if (type == OrderType.BUY) {
            walletEventPublisher.requestDebit(savedOrder.getId(), userId, "USD", totalCost);
        }

        return savedOrder;
    }

    public List<Order> getUserOrders(UUID userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<OrderHistory> getOrderHistory(UUID id) {
        List<OrderHistory> domainHistoryList = orderRepository.findHistoryById(id);

        return domainHistoryList.stream()
                .map(h -> new OrderHistory(
                        h.order(),
                        h.revisionNumber(),
                        h.revisionType()
                ))
                .toList();
    }
}