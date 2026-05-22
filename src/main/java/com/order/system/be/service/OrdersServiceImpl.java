package com.order.system.be.service;

import com.order.system.be.config.RabbitMQConfig;
import com.order.system.be.dto.orderDto.*;
import com.order.system.be.entity.OrderItems;
import com.order.system.be.entity.Orders;
import com.order.system.be.entity.Products;
import com.order.system.be.repository.OrdersRepsitory;
import com.order.system.be.repository.ProductsRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService{

    private final OrdersRepsitory ordersRepsitory;
    private final ProductsRepository productsRepository;
    private final RabbitTemplate rabbitTemplate;

    public OrdersServiceImpl(OrdersRepsitory ordersRepsitory,
                             ProductsRepository productsRepository,
                             RabbitTemplate rabbitTemplate){
        this.ordersRepsitory = ordersRepsitory;
        this.productsRepository = productsRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Orders orders = new Orders();
        orders.setUserId(request.getUserId());
        orders.setStatus("PENDING");


        orders.setOrderItemsList(new ArrayList<>());

        try {
            List<OrderItems> orderItemsList = new ArrayList<>();
            int calculatedTotalAmount = 0;

            for(OrderItemRequest orderItemRequest : request.getListOrderItemsRequest()){

                if(orderItemRequest.getQuantity() == null || orderItemRequest.getQuantity() <= 0){
                    throw new RuntimeException(String.format("Quantity %d harus lebih dari 0", orderItemRequest.getQuantity()));
                }

                Products product = productsRepository.findById(orderItemRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product dengan ID " + orderItemRequest.getProductId() + " tidak ditemukan"));

                if(product.getStock() < orderItemRequest.getQuantity()){
                    throw new RuntimeException(String.format("Stok untuk produk %s tidak mencukupi. Sisa stok %d", product.getName(), product.getStock()));
                }

                OrderItems orderItems = new OrderItems();
                orderItems.setOrders(orders);
                orderItems.setProduct(product);
                orderItems.setPrice(orderItemRequest.getPrice());
                orderItems.setQuantity(orderItemRequest.getQuantity());

                calculatedTotalAmount += (product.getPrice() * orderItemRequest.getQuantity());
                orderItemsList.add(orderItems);
            }


            orders.setOrderItemsList(orderItemsList);
            orders.setTotalAmount(calculatedTotalAmount);

            ordersRepsitory.save(orders);
            publishOrderCreatedEvent(orders);

            return mapToOrderResponse(orders);

        } catch (RuntimeException e) {
            System.err.println("Menangkap error validasi stok awal: " + e.getMessage());

            orders.setStatus("FAILED");


            orders.setTotalAmount(0);


            Orders savedFailedOrder = ordersRepsitory.save(orders);


            OrderProcessEvent failedEvent = new OrderProcessEvent(savedFailedOrder.getOrdersId(), "FAILED");
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_ORDER,
                    RabbitMQConfig.ROUTING_ORDER_FAILED,
                    failedEvent
            );


            return mapToOrderResponse(savedFailedOrder);
        }
    }

    @Override
    public OrderResponse findOrderDetail(Long id) {
        Orders orders = ordersRepsitory.findById(id).orElseThrow(() -> new RuntimeException("Order Id tidak ditemukan"));
        return mapToOrderResponse(orders);
    }

    private void publishOrderCreatedEvent(Orders order) {
        List<OrderItemEvent> eventItems = order.getOrderItemsList().stream()
                .map(item -> new OrderItemEvent(
                        item.getProduct().getProductId(),
                        item.getQuantity()))
                .collect(Collectors.toList());

        OrderCreatedEvent eventMessage = new OrderCreatedEvent(
                order.getOrdersId(),
                order.getUserId(),
                eventItems
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_ORDER,
                RabbitMQConfig.ROUTING_ORDER_CREATED,
                eventMessage
        );
        System.out.println("Event order.created successfully published for Order ID: " + order.getOrdersId());
    }

    private OrderResponse mapToOrderResponse(Orders orders) {
        List<OrderItemResponse> itemResponses = new ArrayList<>();

        if (orders.getOrderItemsList() != null) {
            itemResponses = orders.getOrderItemsList().stream().map(item ->
                    new OrderItemResponse(
                            orders.getOrdersId(),
                            item.getProduct().getProductId(),
                            item.getQuantity(),
                            item.getPrice()
                    )
            ).collect(Collectors.toList());
        }

        return new OrderResponse(
                orders.getOrdersId(),
                orders.getUserId(),
                orders.getTotalAmount(),
                orders.getStatus(),
                orders.getCreatedAt(),
                itemResponses
        );
    }

    @Override
    @Transactional
    public void executeOrderProcessing(OrderCreatedEvent event) {
        Orders order = ordersRepsitory.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Data Order tidak ditemukan"));

        List<Products> productsToUpdate = new ArrayList<>();

        for (OrderItemEvent item : event.getItems()) {
            Products product = productsRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product tidak ditemukan"));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Stok tidak mencukupi untuk produk: " + product.getName());
            }

            product.setStock(product.getStock() - item.getQuantity());
            productsToUpdate.add(product);
        }

        productsRepository.saveAll(productsToUpdate);

        order.setStatus("PAID");
        ordersRepsitory.save(order);
    }

    @Override
    @Transactional
    public void updateOrderStatusToFailed(Long orderId) {
        ordersRepsitory.findById(orderId).ifPresent(order -> {
            order.setStatus("FAILED");
            ordersRepsitory.save(order);
        });
    }
}
