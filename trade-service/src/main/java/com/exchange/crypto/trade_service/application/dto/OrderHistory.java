package com.exchange.crypto.trade_service.application.dto;

import com.exchange.crypto.trade_service.domain.entity.Order;

public record OrderHistory(Order order, Number revisionNumber, String revisionType) {}