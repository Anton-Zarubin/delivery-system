package ru.skillbox.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.dto.OrderDetailsDto;
import ru.skillbox.orderservice.dto.OrderDto;
import ru.skillbox.orderservice.domain.OrderStatus;
import ru.skillbox.orderservice.dto.StatusDto;
import ru.skillbox.orderservice.service.OrderService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Configuration
    @ComponentScan(basePackageClasses = {OrderController.class})
    public static class TestConf {
    }

    private Order order;

    private Order newOrder;

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
        order.setUserId(1L);
        order.addOrderDetails(new HashMap<>(Collections.singletonMap(1L, 1)));
        newOrder = new Order(
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Dubininskaya 39",
                "Order #342",
                BigDecimal.TEN,
                OrderStatus.REGISTERED
        );
        newOrder.addOrderDetails(new HashMap<>(Collections.singletonMap(2L, 1)));
        orders = Collections.singletonList(order);
    }

    @Test
    public void getAllOrders() throws Exception {
        when(orderService.getAllOrders(1L, false, PageRequest.of(0, 1)))
                .thenReturn(new PageImpl<>(orders));
        mvc.perform(
                        get("/order")
                                .with(request -> {
                                    request.addHeader("id", 1L);
                                    request.addHeader("roles", "ROLE_USER");
                                    return request;
                                })
                                .param("page", "0")
                                .param("size", "1")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(order.getDescription())));
    }

    @Test
    public void getOrder() throws Exception {
        when(orderService.getOrder(1L)).thenReturn(order);
        mvc.perform(get("/order/1")
                        .with(request -> {
                            request.addHeader("id", 1L);
                            request.addHeader("roles", "ROLE_USER");
                            return request;
                        })
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(order.getDescription())));
    }

    @Test
    public void getOrderWithException() throws Exception {
        when(orderService.getOrder(1L)).thenReturn(order);
        mvc.perform(get("/order/1")
                        .with(request -> {
                            request.addHeader("id", 2L);
                            request.addHeader("roles", "ROLE_USER");
                            return request;
                        })
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void addOrder() throws Exception {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto(2L, 1);
        OrderDto orderDto = new OrderDto(
                "Order #342",
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Dubininskaya 39",
                List.of(orderDetailsDto),
                BigDecimal.TEN
        );
        when(orderService.addOrder(1L, orderDto)).thenReturn(Optional.of(newOrder));
        mvc.perform(
                        post("/order")
                                .with(request -> {
                                    request.addHeader("id", 1L);
                                    return request;
                                })
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(orderDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    public void updateOrderStatus() throws Exception {
        StatusDto statusDto = new StatusDto(OrderStatus.PAID);
        doNothing().when(orderService).updateOrderStatus(1L, statusDto);
        mvc.perform(
                        patch("/order/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\"status\": \"PAID\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void updateOrderStatusWithError() throws Exception {
        StatusDto statusDto = new StatusDto(OrderStatus.PAID);
        doThrow(new RuntimeException()).when(orderService).updateOrderStatus(1L, statusDto);
        mvc.perform(
                        patch("/order/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\"status\": \"PAID\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotModified());
    }

}
