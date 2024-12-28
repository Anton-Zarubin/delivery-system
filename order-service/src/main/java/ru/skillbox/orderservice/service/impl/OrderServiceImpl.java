package ru.skillbox.orderservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.orderservice.exception.OrderNotFoundException;
import ru.skillbox.orderservice.domain.*;
import ru.skillbox.orderservice.dto.OrderDto;
import ru.skillbox.orderservice.dto.OrderKafkaDto;
import ru.skillbox.orderservice.dto.StatusDto;
import ru.skillbox.orderservice.repository.OrderRepository;
import ru.skillbox.orderservice.service.KafkaService;
import ru.skillbox.orderservice.service.OrderService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final KafkaService kafkaService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, KafkaService kafkaService) {
        this.orderRepository = orderRepository;
        this.kafkaService = kafkaService;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional
    @Override
    public Optional<Order> addOrder(OrderDto orderDto) {
        Order newOrder = new Order(
                orderDto.getDepartureAddress(),
                orderDto.getDestinationAddress(),
                orderDto.getDescription(),
                orderDto.getCost(),
                OrderStatus.REGISTERED
        );
        newOrder.addStatusHistory(newOrder.getStatus(), ServiceName.ORDER_SERVICE, "Order created");
        Order order = orderRepository.saveAndFlush(newOrder);
        kafkaService.produce(OrderKafkaDto.toKafkaDto(order));
        return Optional.of(order);
    }

    @Transactional
    @Override
    public void updateOrderStatus(Long id, StatusDto statusDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        if (order.getStatus() == statusDto.getStatus()) {
            log.info("Request with same status {} for order {} from service {}", statusDto.getStatus(), id, statusDto.getServiceName());
            return;
        }
        order.setStatus(statusDto.getStatus());
        order.addStatusHistory(statusDto.getStatus(), statusDto.getServiceName(), statusDto.getComment());
        Order resultOrder = orderRepository.saveAndFlush(order);
        kafkaService.produce(OrderKafkaDto.toKafkaDto(resultOrder));
    }
}
