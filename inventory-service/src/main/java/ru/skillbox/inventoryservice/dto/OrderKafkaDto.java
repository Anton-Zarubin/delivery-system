package ru.skillbox.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderKafkaDto {

    private Long orderId;

    private StatusDto statusDto;
}
