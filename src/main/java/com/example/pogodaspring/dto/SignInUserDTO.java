package com.example.pogodaspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignInUserDTO(
        @NotNull(message = "Введите логин")
        @NotBlank(message = "Поле не может быть пустым или содержать только пробелы.")
        @Size(min = 5, max = 10, message = "Логин должен быть от 5 до 10 символов.")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Логин должен содержать только латинские буквы и цифры.")
        String username,
        @NotNull(message = "Введите пароль")
        @NotBlank(message = "Поле не может быть пустым или содержать только пробелы.")
        @Size(min = 8, max = 25, message = "Пароль должен иметь от 8 до 25 символов")
        String password) {
}
