package com.exchange.crypto.wallet_service.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE = "exchange.events";
    public static final String WALLET_DEBIT_QUEUE = "wallet.debit.requested.queue";
    public static final String WALLET_DEBIT_ROUTING_KEY = "wallet.debit.requested";
    public static final String DEAD_LETTER_EXCHANGE = "exchange.events.dlx";
    public static final String DEAD_LETTER_QUEUE = "wallet.debit.requested.dlq";
    public static final String DEAD_LETTER_ROUTING_KEY = "wallet.debit.failed";

    @Bean
    TopicExchange eventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue walletDebitQueue() {
        return QueueBuilder.durable(WALLET_DEBIT_QUEUE)
            .deadLetterExchange(DEAD_LETTER_EXCHANGE)
            .deadLetterRoutingKey(DEAD_LETTER_ROUTING_KEY)
            .build();
    }

    @Bean
    Binding walletDebitBinding(Queue walletDebitQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(walletDebitQueue)
                .to(eventsExchange)
                .with(WALLET_DEBIT_ROUTING_KEY);
    }

    @Bean
    TopicExchange deadLetterExchange() {
        return new TopicExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE, true);
    }

    @Bean
    Binding deadLetterBinding(Queue deadLetterQueue, TopicExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(DEAD_LETTER_ROUTING_KEY);
    }

    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
