package ru.skillbox.orderservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.dto.OrderDto;
import ru.skillbox.orderservice.dto.StatusDto;

import java.util.Optional;

public interface OrderService {

    Page<Order> getAllOrders(Long userId, boolean isAdmin, Pageable pageable);

    Order getOrder(Long id);

    Optional<Order> addOrder(Long userId, OrderDto orderDto);

    void updateOrderStatus(Long id, StatusDto statusDto);
}
