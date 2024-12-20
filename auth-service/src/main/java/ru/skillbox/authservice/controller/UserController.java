package ru.skillbox.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.authservice.aop.annotation.CheckOwnership;
import ru.skillbox.authservice.dto.UserDto;
import ru.skillbox.authservice.domain.User;
import ru.skillbox.authservice.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get user by name", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @Operation(summary = "Get all users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Create new user")
    @PostMapping(value = "/signup")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userDto));
    }

    @Operation(summary = "Delete user by name", security = @SecurityRequirement(name = "bearerAuth"))
    @CheckOwnership(entityType = "user")
    @DeleteMapping(value = "/delete/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
