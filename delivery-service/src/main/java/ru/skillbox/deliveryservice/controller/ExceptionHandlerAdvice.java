package ru.skillbox.deliveryservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.deliveryservice.dto.ErrorDto;
import ru.skillbox.deliveryservice.exception.DeliveryFailedException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({DeliveryFailedException.class})
    public ResponseEntity<ErrorDto> exceptionHandler(Exception ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.badRequest().body(errorDto);
    }
}