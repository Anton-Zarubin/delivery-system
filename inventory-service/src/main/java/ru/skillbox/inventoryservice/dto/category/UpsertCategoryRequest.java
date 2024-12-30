package ru.skillbox.inventoryservice.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertCategoryRequest {

    @NotBlank(message = "Category title must not be blank")
    private String title;
}
