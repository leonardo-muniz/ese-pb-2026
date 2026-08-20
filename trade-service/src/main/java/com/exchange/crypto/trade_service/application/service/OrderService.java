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
import com.exchange.crypto.trade_service.infrastructure.client.WalletClient;
import com.exchange.crypto.trade_service.infrastructure.client.WalletResponse;
import com.exchange.crypto.trade_service.infrastructure.client.WalletTransactionRequest;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WalletClient walletClient;

    public OrderService(OrderRepository orderRepository, WalletClient walletClient) {
        this.orderRepository = orderRepository;
        this.walletClient = walletClient;
    }

    public Order createOrder(UUID userId, OrderType type, String cryptoCurrency, BigDecimal amount, BigDecimal price) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0 || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade e o preço devem ser maiores que zero.");
        }

        List<WalletResponse> userWallets = walletClient.getUserWallets(userId);
        if (userWallets.isEmpty()) {
            throw new IllegalArgumentException("Usuário não possui carteiras configuradas para operar.");
        }

        BigDecimal totalCost = amount.multiply(price);

        if (type == OrderType.BUY) {
            WalletResponse fiatWallet = userWallets.stream()
                    .filter(w -> w.currency().equalsIgnoreCase("USD"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não possui uma carteira de Dólar (USD)."));

            if (fiatWallet.balance().compareTo(totalCost) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente na carteira USD. Custo total: $" + totalCost);
            }

            walletClient.withdraw(fiatWallet.id(), new WalletTransactionRequest(totalCost));
        }

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

        return orderRepository.save(newOrder);
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