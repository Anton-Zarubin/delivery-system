package ru.skillbox.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplenishmentDto {

    @Min(value = 1, message = "The amount for replenishment cannot be less than 1")
    private BigDecimal amount;
}
