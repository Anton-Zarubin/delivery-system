package ru.skillbox.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorKafkaDto {

    private Long orderId;

    private StatusDto statusDto;
}
