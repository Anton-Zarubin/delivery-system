package ru.skillbox.orderservice.exception;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) { super(message); }
}
