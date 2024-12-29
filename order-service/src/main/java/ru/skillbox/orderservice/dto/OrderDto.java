package ru.skillbox.orderservice.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderDto {

    private String description;

    @NotBlank(message = "Departure address is required")
    private String departureAddress;

    @NotBlank(message = "Destination address is required")
    private String destinationAddress;

    @Positive(message = "Cost must be positive")
    private BigDecimal cost;
}
