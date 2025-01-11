package com.example.pogodaspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignUpUserDTO(
        @NotNull(message = "Поле не может быть null.")
        @NotBlank(message = "Поле не может быть пустым или содержать только пробелы.")
        String username,
        @NotNull(message = "Поле не может быть null.")
        @NotBlank(message = "Поле не может быть пустым или содержать только пробелы.")
        @Size(min = 8, max = 25, message = "Пароль должен иметь от 8 до 25 символов")
        String password,
        @NotNull(message = "Поле не может быть null.")
        @NotBlank(message = "Поле не может быть пустым или содержать только пробелы.")
        String confirmPassword) {
}
