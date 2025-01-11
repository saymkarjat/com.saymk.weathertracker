package com.example.pogodaspring.validator;

import com.example.pogodaspring.dto.SignInUserDTO;
import com.example.pogodaspring.service.SignInService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SignInUserDtoValidator implements Validator {
    private final SignInService signInService;

    public SignInUserDtoValidator(SignInService signInService) {
        this.signInService = signInService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return SignInUserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignInUserDTO userDTO = (SignInUserDTO) target;

        if (!signInService.isUserCredentialsValid(userDTO)) {
            errors.rejectValue("password", "password.mismatch", "Неправильно введен логин или пароль");
        }
    }
}

