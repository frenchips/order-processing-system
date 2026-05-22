package com.order.system.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.sql.Timestamp;
import java.util.List;

@Table(name = "orders")
@Entity
@Getter
@Setter
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordersId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany( cascade = CascadeType.ALL,  fetch = FetchType.LAZY, mappedBy = "orders", orphanRemoval = true)
    List<OrderItems> orderItemsList;
}
