package com.exchange.crypto.trade.application.dto;

import com.exchange.crypto.trade.domain.entity.Order;

public record OrderHistory(Order order, Number revisionNumber, String revisionType) {}