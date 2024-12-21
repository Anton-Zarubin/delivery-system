package ru.skillbox.inventoryservice.exception;

public class NotEnoughProductException extends RuntimeException {

    public NotEnoughProductException(String message) {
        super(message);
    }
}