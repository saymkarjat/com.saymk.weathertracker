package com.example.pogodaspring.controller;

import com.example.pogodaspring.dto.SignUpUserDTO;
import com.example.pogodaspring.exception.UserAlreadyExistException;
import com.example.pogodaspring.service.SessionService;
import com.example.pogodaspring.service.SignUpService;
import com.example.pogodaspring.validator.SignUpUserDtoValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private SessionService sessionService;

    @Autowired
    public SignUpController(SignUpService signUpService, SignUpUserDtoValidator userDtoValidator, SessionService sessionService) {
        this.signUpService = signUpService;
        this.userDtoValidator = userDtoValidator;
        this.sessionService = sessionService;
    }

    @GetMapping("signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("userDTO", SignUpUserDTO.builder().build());
        return "signuppost";

    }

    @PostMapping("signup")
    public String signUp(@ModelAttribute("userDTO") @Validated SignUpUserDTO userDTO, BindingResult bindingResult, HttpServletRequest req, HttpServletResponse resp) {
        userDtoValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "signuppost";
        }
        sessionService.deleteSessionIfNewUserHasBeenRegistered(req, resp);
        try {
            signUpService.signUp(userDTO);
        }catch (UserAlreadyExistException e){
            bindingResult.rejectValue("username", "error.username", "Пользователь с таким именем уже существует");
            return "signuppost";
        }
        return "redirect:/app/home";
    }
}

