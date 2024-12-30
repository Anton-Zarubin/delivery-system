package ru.skillbox.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PaymentKafkaDto {

    private Long userId;

    private Long orderId;

    private List<OrderDetailsDto> products;

    private BigDecimal cost;

    private String departureAddress;

    private String destinationAddress;
}
