package ru.skillbox.paymentservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDetailsDto {

    private Long productId;

    private Integer count;
}
