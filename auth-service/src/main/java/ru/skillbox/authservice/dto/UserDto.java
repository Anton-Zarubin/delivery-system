package ru.skillbox.authservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @Size(min = 5, max = 100, message = "Password must be between 5 and 100 characters")
    private String password;
}
