package ru.skillbox.authservice.exception;

import java.text.MessageFormat;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String name) {
        super(MessageFormat.format("User with name {0} not found", name));
    }
}
