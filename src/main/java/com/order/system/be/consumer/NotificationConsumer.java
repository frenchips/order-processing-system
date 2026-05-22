package com.order.system.be.consumer;

import com.order.system.be.config.RabbitMQConfig;
import com.order.system.be.dto.orderDto.OrderProcessEvent;
import com.order.system.be.entity.Notifications;
import com.order.system.be.repository.NotificationRepository;
import com.order.system.be.repository.OrdersRepsitory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;


@Component
public class NotificationConsumer {
    private final NotificationRepository notificationsRepository;
    private final OrdersRepsitory ordersRepsitory;

    public NotificationConsumer(NotificationRepository notificationsRepository,
                                OrdersRepsitory ordersRepsitory) {
        this.notificationsRepository = notificationsRepository;
        this.ordersRepsitory = ordersRepsitory;
    }


    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_PAID)
    public void consumeOrderPaid(OrderProcessEvent event) {

        ordersRepsitory.findById(event.getOrdersId()).ifPresent(order -> {
            Notifications notification = new Notifications();
            notification.setUserId(order.getUserId());
            notification.setMessage(String.format("Order #%d berhasil dibayar", event.getOrdersId()));
            notification.setIsRead(false);
            notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            notificationsRepository.save(notification);
            System.out.println("Notifikasi sukses disimpan untuk User: " + order.getUserId());
        });
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_FAILED)
    public void consumeOrderFailed(OrderProcessEvent event) {

        ordersRepsitory.findById(event.getOrdersId()).ifPresent(order -> {
            Notifications notification = new Notifications();
            notification.setUserId(order.getUserId());
            notification.setMessage(String.format("Order #%d gagal diproses", event.getOrdersId()));
            notification.setIsRead(false);
            notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            notificationsRepository.save(notification);
            System.out.println("Notifikasi gagal disimpan untuk User: " + order.getUserId());
        });
    }
}
