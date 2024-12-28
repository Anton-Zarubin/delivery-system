package ru.skillbox.authservice.service;

import ru.skillbox.authservice.domain.User;
import ru.skillbox.authservice.dto.UserDto;

import java.util.List;

public interface UserService {

    User getUser(String name);

    List<User> getAllUsers();

    User createUser(UserDto userDto);

    void deleteUser(String name);
}
