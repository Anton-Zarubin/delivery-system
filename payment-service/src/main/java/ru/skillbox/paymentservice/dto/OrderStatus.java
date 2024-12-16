package ru.skillbox.paymentservice.dto;

public enum OrderStatus {

    PAID,
    PAYMENT_FAILED,
    INVENTMENT_FAILED,
    DELIVERY_FAILED,
    UNEXPECTED_FAILURE
}
