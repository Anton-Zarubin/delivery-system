package ru.skillbox.paymentservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class PaymentKafkaDto {

    private Long userId;

    private Long orderId;

    private List<OrderDetailsDto> products;

    private BigDecimal cost;

    private String departureAddress;

    private String destinationAddress;
}
