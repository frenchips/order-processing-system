package com.order.system.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "order_items")
@Entity
@Getter
@Setter
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemsId;

    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @ManyToOne
    private Orders orders;

    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    @ManyToOne
    private Products productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Integer price;
}

