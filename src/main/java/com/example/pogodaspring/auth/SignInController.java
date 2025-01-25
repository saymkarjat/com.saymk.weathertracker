package com.example.pogodaspring.auth;

import com.example.pogodaspring.dto.SessionDTO;
import com.example.pogodaspring.dto.SignInUserDTO;
import com.example.pogodaspring.service.SessionService;
import com.example.pogodaspring.service.SignInService;
import com.example.pogodaspring.validator.SignInUserDtoValidator;
import jakarta.servlet.http.Cookie;
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
import org.springframework.web.util.WebUtils;

import java.util.UUID;

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

    //todo
    @PostMapping("signin")
    public String signIn(@ModelAttribute("userDTO") @Validated SignInUserDTO userDTO, BindingResult bindingResult, HttpServletResponse resp) {
        userDtoValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "signinpost";
        }

        SessionDTO sessionDTO = signInService.authenticate(userDTO);
        Cookie cookie = new Cookie("session_id", sessionDTO.id().toString());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(2 * 3600);
        resp.addCookie(cookie);

        return "redirect:/app/home";
    }

    //todo
    @GetMapping("logout")
    public String logOut(HttpServletRequest req, HttpServletResponse resp) {
        Cookie cookie = WebUtils.getCookie(req, "session_id");
        if (cookie != null) {
            try {
                sessionService.deleteSession(UUID.fromString(cookie.getValue()));
            } catch (IllegalArgumentException e) {
                //log.info
            }
            cookie.setValue("");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            resp.addCookie(cookie);
        }

        return "redirect:/app/home";
    }
}