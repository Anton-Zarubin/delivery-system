package ru.skillbox.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.dto.OrderDto;
import ru.skillbox.orderservice.dto.StatusDto;
import ru.skillbox.orderservice.exception.AccessDeniedException;
import ru.skillbox.orderservice.service.OrderService;
import ru.skillbox.orderservice.utils.RequestHeaderUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Get all orders in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/order")
    public Page<Order> getAllOrders(HttpServletRequest request, Pageable pageable) {
        return orderService.getAllOrders(RequestHeaderUtils.getActiveUserId(request), RequestHeaderUtils.isAdmin(request), pageable);
    }

    @Operation(summary = "Get an order by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/order/{orderId}")
    public Order getOrder(HttpServletRequest request,
                          @PathVariable @Parameter(description = "Id of order") Long orderId) {
        Order order = orderService.getOrder(orderId);
        if(!order.getUserId().equals(RequestHeaderUtils.getActiveUserId(request)) &&
                !RequestHeaderUtils.isAdmin(request)) {
            throw new AccessDeniedException("Only the customer who placed the order can receive information about this order");
        } else {
            return order;
        }
    }

    @Operation(summary = "Add order and start delivery process for it", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/order")
    public ResponseEntity<?> addOrder(HttpServletRequest request, @Valid @RequestBody OrderDto input) {
        return orderService.addOrder(RequestHeaderUtils.getActiveUserId(request), input)
                .map(order -> ResponseEntity.status(HttpStatus.CREATED).body(order))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build());
    }

    @Operation(summary = "Update order status", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/order/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable @Parameter(description = "Id of order") long orderId,
                                                  @RequestBody StatusDto statusDto) {
        try {
            orderService.updateOrderStatus(orderId, statusDto);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Can't change status for order with id {}", orderId, ex);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }
}
