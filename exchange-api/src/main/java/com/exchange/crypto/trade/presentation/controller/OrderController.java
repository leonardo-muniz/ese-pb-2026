package com.exchange.crypto.trade.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exchange.crypto.trade.application.dto.OrderHistory;
import com.exchange.crypto.trade.application.dto.OrderRequest;
import com.exchange.crypto.trade.application.service.OrderService;
import com.exchange.crypto.trade.domain.entity.Order;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest request) {
        Order order = orderService.createOrder(
                request.userId(),
                request.type(),
                request.cryptoCurrency(),
                request.amount(),
                request.price()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable UUID userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    // Buscar o histórico de auditoria do Hibernate Envers
    @GetMapping("/{id}/history")
    public ResponseEntity<List<OrderHistory>> getOrderHistory(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderHistory(id));
    }
}