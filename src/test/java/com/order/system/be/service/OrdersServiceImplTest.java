package com.order.system.be.service;

import com.order.system.be.dto.orderDto.*;
import com.order.system.be.entity.Orders;
import com.order.system.be.entity.Products;
import com.order.system.be.repository.OrdersRepsitory;
import com.order.system.be.repository.ProductsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdersServiceImplTest {

    @Mock
    private OrdersRepsitory ordersRepsitory;

    @Mock
    private ProductsRepository productsRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrdersServiceImpl ordersService;

    private Products product;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        product = new Products();
        product.setProductId(1L);
        product.setName("Test Product");
        product.setPrice(100);
        product.setStock(10);

        OrderItemRequest itemRequest = new OrderItemRequest(null, 1L, 2, 100);

        orderRequest = new OrderRequest("1", Collections.singletonList(itemRequest));
    }

    @Test
    void createOrder_Success() {
        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));
        when(ordersRepsitory.save(any(Orders.class))).thenAnswer(invocation -> {
            Orders order = invocation.getArgument(0);
            order.setOrdersId(1L);
            return order;
        });

        OrderResponse response = ordersService.createOrder(orderRequest);

        assertNotNull(response);
        assertEquals(1L, response.getOrderId());
        assertEquals("PENDING", response.getStatus());
        assertEquals(200, response.getTotalAmount());
        verify(ordersRepsitory, times(1)).save(any(Orders.class));
        verify(rabbitTemplate, times(1)).convertAndSend(any(), any(), any(OrderCreatedEvent.class));
    }

    @Test
    void createOrder_InsufficientStock() {
        product.setStock(1);
        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));
        when(ordersRepsitory.save(any(Orders.class))).thenAnswer(invocation -> {
            Orders order = invocation.getArgument(0);
            order.setOrdersId(1L);
            return order;
        });

        OrderResponse response = ordersService.createOrder(orderRequest);

        assertNotNull(response);
        assertEquals("FAILED", response.getStatus());
        assertEquals(0, response.getTotalAmount());
        verify(ordersRepsitory, times(1)).save(any(Orders.class));
        verify(rabbitTemplate, times(1)).convertAndSend(any(), any(), any(OrderProcessEvent.class));
    }

    @Test
    void findOrderDetail_Success() {
        Orders order = new Orders();
        order.setOrdersId(1L);
        order.setUserId("1");
        order.setStatus("PENDING");
        order.setOrderItemsList(new ArrayList<>());

        when(ordersRepsitory.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = ordersService.findOrderDetail(1L);

        assertNotNull(response);
        assertEquals(1L, response.getOrderId());
        assertEquals("PENDING", response.getStatus());
    }

    @Test
    void findOrderDetail_NotFound() {
        when(ordersRepsitory.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ordersService.findOrderDetail(1L));
    }

    @Test
    void executeOrderProcessing_Success() {
        Orders order = new Orders();
        order.setOrdersId(1L);
        order.setStatus("PENDING");

        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(1L);
        OrderItemEvent itemEvent = new OrderItemEvent(1L, 2);
        event.setItems(Collections.singletonList(itemEvent));

        when(ordersRepsitory.findById(1L)).thenReturn(Optional.of(order));
        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));

        ordersService.executeOrderProcessing(event);

        assertEquals("PAID", order.getStatus());
        assertEquals(8, product.getStock());
        verify(productsRepository, times(1)).saveAll(any());
        verify(ordersRepsitory, times(1)).save(order);
    }

    @Test
    void updateOrderStatusToFailed_Success() {
        Orders order = new Orders();
        order.setOrdersId(1L);
        order.setStatus("PENDING");

        when(ordersRepsitory.findById(1L)).thenReturn(Optional.of(order));

        ordersService.updateOrderStatusToFailed(1L);

        assertEquals("FAILED", order.getStatus());
        verify(ordersRepsitory, times(1)).save(order);
    }
}
