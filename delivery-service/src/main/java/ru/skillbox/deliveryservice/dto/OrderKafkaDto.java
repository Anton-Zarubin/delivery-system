package ru.skillbox.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderKafkaDto {

    private Long orderId;

    private StatusDto statusDto;
}
