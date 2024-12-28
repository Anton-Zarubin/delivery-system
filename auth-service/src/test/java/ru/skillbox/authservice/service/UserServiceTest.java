package ru.skillbox.authservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.skillbox.authservice.config.TestConfig;
import ru.skillbox.authservice.domain.Role;
import ru.skillbox.authservice.domain.User;
import ru.skillbox.authservice.dto.UserDto;
import ru.skillbox.authservice.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    private User newUser;

    private List<User> users;

    @BeforeEach
    public void setUp() {
        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0) + "_some_fake_encoding");
        user = new User(
                "Petrov",
                passwordEncoder.encode("superpass"),
                Collections.singletonList(new Role("ROLE_USER"))
        );
        newUser = new User(
                "Ivanov",
                passwordEncoder.encode("superpass99"),
                Collections.singletonList(new Role("ROLE_USER"))
        );
        users = Collections.singletonList(user);
    }

    @Test
    void whenUserExists_thenReturnUser() {
        when(userRepository.findByName("Petrov")).thenReturn(Optional.ofNullable(user));
        assertDoesNotThrow(() -> userService.getUser("Petrov"));
    }

    @Test
    void whenUserNotFound_thenException() {
        when(userRepository.findByName("Sidorov")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUser("Sidorov"));
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(users);
        assertDoesNotThrow(() -> userService.getAllUsers());
    }

    @Test
    void createUser() {
        UserDto userDto = new UserDto();
        userDto.setName("Ivanov");
        userDto.setPassword("superpass99");

        when(userRepository.save(any(User.class))).thenReturn(newUser);
        assertDoesNotThrow(() -> userService.createUser(userDto));
    }
}
