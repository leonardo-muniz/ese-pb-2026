package com.exchange.crypto.trade.service;

import com.exchange.crypto.trade.model.Order;
import com.exchange.crypto.trade.model.OrderStatus;
import com.exchange.crypto.trade.model.OrderType;
import com.exchange.crypto.trade.repository.OrderRepository;
import com.exchange.crypto.wallet.model.Wallet;
import com.exchange.crypto.wallet.service.WalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WalletService walletService;

    public OrderService(OrderRepository orderRepository, WalletService walletService) {
        this.orderRepository = orderRepository;
        this.walletService = walletService;
    }

    public Order createOrder(UUID userId, OrderType type, String cryptoCurrency, BigDecimal amount, BigDecimal price) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0 || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade e o preço devem ser maiores que zero.");
        }

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

        Order newOrder = Order.builder()
                .userId(userId)
                .type(type)
                .cryptoCurrency(cryptoCurrency)
                .amount(amount)
                .price(price)
                .status(OrderStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        return orderRepository.save(newOrder);
    }

    public List<Order> getUserOrders(UUID userId) {
        return orderRepository.findByUserId(userId);
    }
}