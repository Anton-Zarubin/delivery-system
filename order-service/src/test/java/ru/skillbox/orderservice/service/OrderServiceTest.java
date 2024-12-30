package ru.skillbox.orderservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.skillbox.orderservice.config.TestConfig;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.OrderStatus;
import ru.skillbox.orderservice.dto.OrderDetailsDto;
import ru.skillbox.orderservice.dto.OrderDto;
import ru.skillbox.orderservice.dto.StatusDto;
import ru.skillbox.orderservice.repository.OrderRepository;
import ru.skillbox.orderservice.repository.OrderSpecifications;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    private List<Order> orders;

    @BeforeEach
    public void setUp() {
        order = new Order(
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Tulskaya 24",
                "Order #112",
                BigDecimal.ONE,
                OrderStatus.REGISTERED
        );
        order.setId(1L);
        order.setUserId(1L);
        order.setCreationTime(LocalDateTime.now());
        order.setModifiedTime(LocalDateTime.now());
        order.addOrderDetails(new HashMap<>(Collections.singletonMap(1L, 1)));

        orders = Collections.singletonList(order);
    }

    @Test
    void getAllOrders() {
        when(orderRepository.findAll(OrderSpecifications.byUser(1L), Pageable.ofSize(1)))
                .thenReturn(new PageImpl<>(orders));
        assertDoesNotThrow(() -> orderService.getAllOrders(1L, false, Pageable.ofSize(1)));
    }

    @Test
    void whenOrderExists_thenReturnOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(order));
        assertDoesNotThrow(() -> orderService.getOrder(1L));
    }

    @Test
    void whenOrderNotFound_thenException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> orderService.getOrder(1L));
    }

    @Test
    void addOrder() {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto(1L, 1);
        OrderDto orderDto = new OrderDto(
                "Order #112",
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Tulskaya 24",
                List.of(orderDetailsDto),
                BigDecimal.ONE
        );
        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(order);

        assertDoesNotThrow(() -> orderService.addOrder(1L, orderDto));
    }

    @Test
    void updateOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        assertDoesNotThrow(() -> orderService.updateOrderStatus(1L, new StatusDto(OrderStatus.PAID)));
    }

    @Test
    void updateOrderStatusWithError() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                orderService.updateOrderStatus(1L, new StatusDto(OrderStatus.PAID)));
    }
}
