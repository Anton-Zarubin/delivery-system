package ru.skillbox.orderservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {

    private OrderStatus status;

    private ServiceName serviceName;

    private String comment;

    public StatusDto(OrderStatus status) {
        this.status = status;
    }
}