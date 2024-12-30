package ru.skillbox.inventoryservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class InventoryKafkaDto {

    private Long userId;

    private Long orderId;

    private List<OrderDetailsDto> products;

    private String departureAddress;

    private String destinationAddress;
}