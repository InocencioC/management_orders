package com.management.orders.service;

import com.management.orders.domain.entity.Order;
import com.management.orders.domain.entity.OrderStatus;
import com.management.orders.domain.entity.StockMovement;
import com.management.orders.repository.OrderRepository;
import com.management.orders.repository.StockMovementRepository;
import com.management.orders.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final StockMovementRepository stockMovementRepository;
    private final JmsTemplate jmsTemplate;


    private static final String EMAIL_QUEUE = "orders.notification.queue";

    @Transactional
    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);

        log.info("Order created with ID : {}", savedOrder.getId());
        tryToFulfillOrder(savedOrder);
        return savedOrder;
    }

    private void tryToFulfillOrder(Order order) {
        if (order.getStatus() == OrderStatus.COMPLETED) {
            return;
        }
        UUID itemId = order.getItem().getId();
        Integer requiredQuantity = order.getQuantity() - order.getFulfilledQuantity();

        Integer currentStock = stockMovementRepository.getCurrentQuantity(itemId);

        if (currentStock >= requiredQuantity) {
            StockMovement stockMovement = stockMovementRepository.findById(itemId).get();
            stockMovement.setItem(order.getItem());
            stockMovement.setQuantity(-requiredQuantity);
            stockMovement.setOrder(order);

            StockMovement savedStockMovement = stockMovementRepository.save(stockMovement);


        }
    }


}
