package ru.skillbox.paymentservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InventoryKafkaDto {

    private Long userId;

    private Long orderId;

    private List<OrderDetailsDto> products;

    private String departureAddress;

    private String destinationAddress;
}
