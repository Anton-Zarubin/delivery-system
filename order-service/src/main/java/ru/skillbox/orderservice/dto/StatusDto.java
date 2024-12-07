package ru.skillbox.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.orderservice.domain.OrderStatus;
import ru.skillbox.orderservice.domain.ServiceName;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDto {

    private OrderStatus status;

    private ServiceName serviceName;

    private String comment;

    public StatusDto(OrderStatus status) {
        this.status = status;
    }
}
