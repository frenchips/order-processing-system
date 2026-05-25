package com.order.system.be.consumer;

import com.order.system.be.dto.orderDto.OrderProcessEvent;
import com.order.system.be.entity.Notifications;
import com.order.system.be.entity.Orders;
import com.order.system.be.repository.NotificationRepository;
import com.order.system.be.repository.OrdersRepsitory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationConsumerTest {

    @Mock
    private NotificationRepository notificationsRepository;

    @Mock
    private OrdersRepsitory ordersRepsitory;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    @Test
    void consumeOrderPaid_Success() {
        OrderProcessEvent event = new OrderProcessEvent(1L, "PAID");
        Orders order = new Orders();
        order.setOrdersId(1L);
        order.setUserId("1");

        when(ordersRepsitory.findById(1L)).thenReturn(Optional.of(order));

        notificationConsumer.consumeOrderPaid(event);

        verify(notificationsRepository, times(1)).save(any(Notifications.class));
    }

    @Test
    void consumeOrderFailed_Success() {
        OrderProcessEvent event = new OrderProcessEvent(1L, "FAILED");
        Orders order = new Orders();
        order.setOrdersId(1L);
        order.setUserId("1");

        when(ordersRepsitory.findById(1L)).thenReturn(Optional.of(order));

        notificationConsumer.consumeOrderFailed(event);

        verify(notificationsRepository, times(1)).save(any(Notifications.class));
    }
}
