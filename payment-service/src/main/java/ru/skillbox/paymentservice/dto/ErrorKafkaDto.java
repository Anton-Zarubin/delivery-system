package ru.skillbox.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorKafkaDto {

    private Long orderId;

    private StatusDto statusDto;
}
