package com.order.system.be.consumer;

import com.order.system.be.config.RabbitMQConfig;
import com.order.system.be.dto.orderDto.OrderCreatedEvent;
import com.order.system.be.dto.orderDto.OrderProcessEvent;
import com.order.system.be.service.OrdersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderConsumerTest {

    @Mock
    private OrdersService ordersService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderConsumer orderConsumer;

    @Test
    void consumeOrderCreated_Success() {
        OrderCreatedEvent event = new OrderCreatedEvent(1L, "1", Collections.emptyList());

        orderConsumer.consumeOrderCreated(event);

        verify(ordersService, times(1)).executeOrderProcessing(event);
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_ORDER),
                eq(RabbitMQConfig.ROUTING_ORDER_PAID),
                any(OrderProcessEvent.class)
        );
    }

    @Test
    void consumeOrderCreated_Failure() {
        OrderCreatedEvent event = new OrderCreatedEvent(1L, "1", Collections.emptyList());
        doThrow(new RuntimeException("Error")).when(ordersService).executeOrderProcessing(event);

        orderConsumer.consumeOrderCreated(event);

        verify(ordersService, times(1)).updateOrderStatusToFailed(1L);
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_ORDER),
                eq(RabbitMQConfig.ROUTING_ORDER_FAILED),
                any(OrderProcessEvent.class)
        );
    }
}
