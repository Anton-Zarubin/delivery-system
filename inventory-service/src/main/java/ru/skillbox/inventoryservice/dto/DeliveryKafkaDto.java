package ru.skillbox.inventoryservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryKafkaDto {

    private Long orderId;

    private Long invoiceId;

    private String departureAddress;

    private String destinationAddress;
}