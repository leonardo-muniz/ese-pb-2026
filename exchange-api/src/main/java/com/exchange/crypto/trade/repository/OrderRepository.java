package com.exchange.crypto.trade.repository;

import com.exchange.crypto.trade.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final List<Order> orders = new ArrayList<>();

    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(UUID.randomUUID());
        } else {
            orders.removeIf(o -> o.getId().equals(order.getId()));
        }
        orders.add(order);
        return order;
    }

    public List<Order> findByUserId(UUID userId) {
        return orders.stream()
                .filter(o -> o.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}