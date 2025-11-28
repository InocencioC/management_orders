package com.management.orders.repository;

import com.management.orders.domain.entity.Order;
import com.management.orders.domain.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserId(UUID userId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByUserIdAndStatus(UUID userId, OrderStatus status);
}
