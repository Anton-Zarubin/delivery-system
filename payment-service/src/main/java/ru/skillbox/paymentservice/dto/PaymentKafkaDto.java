package ru.skillbox.paymentservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PaymentKafkaDto {

    private Long userId;

    private Long orderId;

    private BigDecimal cost;

    private String departureAddress;

    private String destinationAddress;
}
