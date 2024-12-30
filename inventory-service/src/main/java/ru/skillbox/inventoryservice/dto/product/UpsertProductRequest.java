package ru.skillbox.inventoryservice.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertProductRequest {

    @NotBlank(message = "Product title must not be blank")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Positive(message = "Price per unit must be positive")
    private BigDecimal unitPrice;

    @Min(value = 0, message = "Quantity of product cannot be less than zero")
    private Integer count;

    @NotNull(message = "Category id is required")
    private Long categoryId;
}
