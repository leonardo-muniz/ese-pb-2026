package com.exchange.crypto.trade.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.exchange.crypto.trade.application.dto.OrderHistory;
import com.exchange.crypto.trade.domain.entity.Order;
import com.exchange.crypto.trade.domain.entity.OrderStatus;
import com.exchange.crypto.trade.domain.entity.OrderType;
import com.exchange.crypto.trade.domain.repository.OrderRepository;
import com.exchange.crypto.user.domain.entity.User;
import com.exchange.crypto.user.domain.repository.UserRepository;
import com.exchange.crypto.wallet.application.service.WalletService;
import com.exchange.crypto.wallet.domain.entity.Wallet;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WalletService walletService;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, WalletService walletService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.walletService = walletService;
        this.userRepository = userRepository;
    }

    public Order createOrder(UUID userId, OrderType type, String cryptoCurrency, BigDecimal amount, BigDecimal price) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0 || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade e o preço devem ser maiores que zero.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        List<Wallet> userWallets = walletService.getWalletsByUser(userId);
        if (userWallets.isEmpty()) {
            throw new IllegalArgumentException("Usuário não possui carteiras configuradas para operar.");
        }

        BigDecimal totalCost = amount.multiply(price);

        if (type == OrderType.BUY) {
            Wallet fiatWallet = userWallets.stream()
                    .filter(w -> w.getCurrency().equalsIgnoreCase("USD"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não possui uma carteira de Dólar (USD)."));

            if (fiatWallet.getBalance().compareTo(totalCost) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente na carteira USD. Custo total: $" + totalCost);
            }

            walletService.withdraw(fiatWallet.getId(), totalCost);
        }

        Order newOrder = new Order(
                null,
                user,
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