package ru.skillbox.orderservice.service;

import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.dto.OrderDto;
import ru.skillbox.orderservice.dto.StatusDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrder(Long id);

    Optional<Order> addOrder(OrderDto orderDto);

    void updateOrderStatus(Long id, StatusDto statusDto);
}
