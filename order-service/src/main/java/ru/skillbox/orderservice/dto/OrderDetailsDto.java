package ru.skillbox.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDetailsDto {

    private Long productId;

    private Integer count;
}
