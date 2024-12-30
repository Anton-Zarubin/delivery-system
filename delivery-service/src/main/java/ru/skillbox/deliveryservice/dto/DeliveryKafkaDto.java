package ru.skillbox.deliveryservice.dto;

import lombok.Data;

@Data
public class DeliveryKafkaDto {

    private Long orderId;

    private Long invoiceId;

    private String departureAddress;

    private String destinationAddress;
}
