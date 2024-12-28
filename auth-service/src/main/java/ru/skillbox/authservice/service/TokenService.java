package ru.skillbox.authservice.service;

import org.springframework.security.core.AuthenticationException;
import ru.skillbox.authservice.dto.UserDto;

public interface TokenService {

    String generateToken(UserDto loginUser) throws AuthenticationException;
}
