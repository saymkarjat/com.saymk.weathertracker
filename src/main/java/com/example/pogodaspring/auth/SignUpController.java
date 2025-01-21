package com.example.pogodaspring.auth;

import com.example.pogodaspring.dto.SignUpUserDTO;
import com.example.pogodaspring.exception.UserAlreadyExistException;
import com.example.pogodaspring.service.SignUpService;
import com.example.pogodaspring.validator.SignUpUserDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class SignUpController {
    private SignUpService signUpService;
    private SignUpUserDtoValidator userDtoValidator;


    @Autowired
    public SignUpController(SignUpService signUpService, SignUpUserDtoValidator userDtoValidator) {
        this.signUpService = signUpService;
        this.userDtoValidator = userDtoValidator;
    }

    @GetMapping("signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("userDTO", SignUpUserDTO.builder().build());
        return "signuppost";

    }

    @PostMapping("signup")
    public String signUp(@ModelAttribute("userDTO") @Validated SignUpUserDTO userDTO, BindingResult bindingResult) {
        userDtoValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "signuppost";
        }
        try {
            signUpService.signUp(userDTO);
        } catch (UserAlreadyExistException e) {
            bindingResult.rejectValue("username", "error.username", "Пользователь с таким именем уже существует");
            return "signuppost";
        }
        return "redirect:/auth/logout";
    }
}

