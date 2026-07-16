package com.exchange.crypto.trade.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.exchange.crypto.trade.application.dto.OrderHistory;
import com.exchange.crypto.trade.domain.entity.Order;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(UUID id);
    List<Order> findByUserId(UUID userId);
    List<Order> findAll();
    List<OrderHistory> findHistoryById(UUID id);
}