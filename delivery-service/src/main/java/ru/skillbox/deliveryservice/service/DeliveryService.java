package ru.skillbox.deliveryservice.service;

import ru.skillbox.deliveryservice.dto.DeliveryKafkaDto;

public interface DeliveryService {

    void deliver(DeliveryKafkaDto deliveryKafkaDto);
}
