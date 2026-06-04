package com.exchange.crypto.trade.controller;

import com.exchange.crypto.trade.service.OrderService;
import com.exchange.crypto.trade.model.Order;
import com.exchange.crypto.trade.dto.OrderRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService; }

    // CREATE
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

    // READ
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable UUID userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }
}