package com.example.pogodaspring.controller;

import com.example.pogodaspring.dto.SessionDTO;
import com.example.pogodaspring.dto.SignInUserDTO;
import com.example.pogodaspring.service.SessionService;
import com.example.pogodaspring.service.SignInService;
import com.example.pogodaspring.validator.SignInUserDtoValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class SignInController {

    private final SessionService sessionService;
    private final SignInService signInService;
    private final SignInUserDtoValidator userDtoValidator;

    public SignInController(SessionService sessionService, SignInService signInService, SignInUserDtoValidator userDtoValidator) {
        this.sessionService = sessionService;
        this.signInService = signInService;
        this.userDtoValidator = userDtoValidator;
    }

    @GetMapping("signin")
    public String showSignInForm(Model model) {
        model.addAttribute("userDTO", SignInUserDTO.builder().build());
        return "signinpost";

    }

    @PostMapping("signin")
    public String signIn(@ModelAttribute("userDTO") @Validated SignInUserDTO userDTO, BindingResult bindingResult, HttpServletResponse response) {
        userDtoValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "signinpost";
        }

        SessionDTO sessionDTO = signInService.authenticate(userDTO);
        sessionService.injectCookieIntoResponse(response, sessionDTO.id());

        return "redirect:/app/home";
    }

    @GetMapping("logout")
    public String logOut(HttpServletRequest req, HttpServletResponse resp) {
        sessionService.deleteSessionIfNewUserHasBeenRegistered(req, resp);
        return "redirect:/app/home";
    }
}