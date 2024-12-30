package ru.skillbox.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorDto {

    private String errorMessage;

    private LocalDateTime timestamp;
}
