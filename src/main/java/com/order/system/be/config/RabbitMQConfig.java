package com.order.system.be.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_ORDER_CREATED = "order.created.queue";
    public static final String EXCHANGE_ORDER = "order.exchange";
    public static final String ROUTING_ORDER_CREATED = "order.created.routing";

    public static final String QUEUE_ORDER_PAID = "order.paid.queue";
    public static final String QUEUE_ORDER_FAILED = "order.failed.queue";

    public static final String ROUTING_ORDER_PAID = "order.paid.routing";
    public static final String ROUTING_ORDER_FAILED = "order.failed.routing";

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(QUEUE_ORDER_CREATED, true);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(EXCHANGE_ORDER);
    }

    @Bean
    public Binding bindingOrderCreated(Queue orderCreatedQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderCreatedQueue).to(orderExchange).with(ROUTING_ORDER_CREATED);
    }

    @Bean
    public Queue orderPaidQueue() {
        return new Queue(QUEUE_ORDER_PAID, true);
    }

    @Bean
    public Queue orderFailedQueue() {
        return new Queue(QUEUE_ORDER_FAILED, true);
    }

    @Bean
    public Binding bindingOrderPaid(Queue orderPaidQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderPaidQueue).to(orderExchange).with(ROUTING_ORDER_PAID);
    }

    @Bean
    public Binding bindingOrderFailed(Queue orderFailedQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderFailedQueue).to(orderExchange).with(ROUTING_ORDER_FAILED);
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
