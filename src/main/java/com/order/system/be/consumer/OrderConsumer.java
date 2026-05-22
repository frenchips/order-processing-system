package com.order.system.be.consumer;

import com.order.system.be.config.RabbitMQConfig;
import com.order.system.be.dto.orderDto.OrderCreatedEvent;
import com.order.system.be.dto.orderDto.OrderProcessEvent;
import com.order.system.be.service.OrdersService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {
    private final OrdersService ordersService;
    private final RabbitTemplate rabbitTemplate;

    public OrderConsumer(OrdersService ordersService, RabbitTemplate rabbitTemplate) {
        this.ordersService = ordersService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_CREATED)
    public void consumeOrderCreated(OrderCreatedEvent event) {
        System.out.println("Memproses pesan antrean dari order.created.queue untuk Order ID: " + event.getOrderId());

        try {
            ordersService.executeOrderProcessing(event);

            OrderProcessEvent successEvent = new OrderProcessEvent(event.getOrderId(), "PAID");
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_ORDER,
                    RabbitMQConfig.ROUTING_ORDER_PAID,
                    successEvent
            );
            System.out.println("Berhasil memproses Order ID " + event.getOrderId() + ", status diperbarui menjadi PAID.");

        } catch (Exception e) {
            System.err.println("Gagal memproses Order ID " + event.getOrderId() + " karena: " + e.getMessage());

            ordersService.updateOrderStatusToFailed(event.getOrderId());

            OrderProcessEvent failedEvent = new OrderProcessEvent(event.getOrderId(), "FAILED");
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_ORDER,
                    RabbitMQConfig.ROUTING_ORDER_FAILED,
                    failedEvent
            );
        }
    }
}
