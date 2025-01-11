package com.example.pogodaspring.validator;

import com.example.pogodaspring.dto.SignUpUserDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SignUpUserDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpUserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpUserDTO userDTO = (SignUpUserDTO) target;

        // Проверка логина на латиницу и длину
        if (userDTO.username().length() < 5 || userDTO.username().length() > 10) {
            errors.rejectValue("username", "username.length", "Логин должен быть от 5 до 10 символов.");
        }

        // Проверка на латиницу и цифры
        if (!userDTO.username().matches("^[a-zA-Z0-9]+$")) {
            errors.rejectValue("username", "username.pattern", "Логин должен содержать только латинские буквы и цифры.");
        }

        // Проверка совпадения паролей
        if (!userDTO.password().equals(userDTO.confirmPassword())) {
            errors.rejectValue("confirmPassword", "password.mismatch", "Пароли не совпадают.");
        }
    }
}