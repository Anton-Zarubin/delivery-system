package ru.skillbox.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentKafkaDto {

    private Long userId;

    private Long orderId;

    private BigDecimal cost;

    private String departureAddress;

    private String destinationAddress;
}
