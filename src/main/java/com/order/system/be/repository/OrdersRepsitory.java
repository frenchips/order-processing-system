package com.order.system.be.repository;

import com.order.system.be.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepsitory extends JpaRepository<Orders, Long> {
}
