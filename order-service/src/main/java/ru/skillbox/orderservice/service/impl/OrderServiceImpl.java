package ru.skillbox.orderservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.orderservice.dto.OrderDetailsDto;
import ru.skillbox.orderservice.dto.PaymentKafkaDto;
import ru.skillbox.orderservice.exception.OrderNotFoundException;
import ru.skillbox.orderservice.domain.*;
import ru.skillbox.orderservice.dto.OrderDto;
import ru.skillbox.orderservice.dto.StatusDto;
import ru.skillbox.orderservice.repository.OrderRepository;
import ru.skillbox.orderservice.repository.OrderSpecifications;
import ru.skillbox.orderservice.service.KafkaService;
import ru.skillbox.orderservice.service.OrderService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Page<Order> getAllOrders(Long userId, boolean isAdmin, Pageable pageable) {
        Specification<Order> spec = isAdmin ? null : OrderSpecifications.byUser(userId);
        return orderRepository.findAll(spec, pageable);
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional
    @Override
    public Optional<Order> addOrder(Long userId, OrderDto orderDto) {
        List<OrderDetailsDto> products = orderDto.getProducts();
        Map<Long, Integer> productQuantityMap = orderDto.getProducts()
                .stream()
                .collect(Collectors.toMap(OrderDetailsDto::getProductId, OrderDetailsDto::getCount));
        Order newOrder = new Order(
                orderDto.getDepartureAddress(),
                orderDto.getDestinationAddress(),
                orderDto.getDescription(),
                orderDto.getCost(),
                OrderStatus.REGISTERED
        );
        newOrder.setUserId(userId);
        newOrder.addOrderDetails(productQuantityMap);
        newOrder.addStatusHistory(newOrder.getStatus(), ServiceName.ORDER_SERVICE, "Order created");
        Order order = orderRepository.saveAndFlush(newOrder);
        log.info("Order with id {} was registered at {}", order.getId(),order.getCreationTime());

        kafkaService.produce(PaymentKafkaDto.builder()
                .userId(userId)
                .orderId(order.getId())
                .products(products)
                .cost(order.getCost())
                .departureAddress(order.getDepartureAddress())
                .destinationAddress(order.getDestinationAddress())
                .build());
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
        orderRepository.save(order);
        log.info("Status for order with id {} changed to: {}", id, order.getStatus());
    }
}
