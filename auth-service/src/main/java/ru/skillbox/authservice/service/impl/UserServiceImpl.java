package ru.skillbox.authservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.authservice.domain.User;
import ru.skillbox.authservice.dto.UserDto;
import ru.skillbox.authservice.exception.UserNotFoundException;
import ru.skillbox.authservice.repository.UserRepository;
import ru.skillbox.authservice.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUser(String name) {
        return userRepository.findByName(name).orElseThrow(() ->
                new UserNotFoundException(name));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(UserDto userDto) {
        return userRepository.save(new User(
                userDto.getName(),
                passwordEncoder.encode(userDto.getPassword())));
    }

    @Override
    public void deleteUser(String name) {
        userRepository.deleteByName(name);
    }
}
