package com.example.pogodaspring.service;

import com.example.pogodaspring.dto.SessionDTO;
import com.example.pogodaspring.dto.SignInUserDTO;
import com.example.pogodaspring.model.User;
import com.example.pogodaspring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class SignInService {
    //todo
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final SessionService sessionService;

    @Autowired
    public SignInService(UserRepository userRepository, PasswordService passwordService, SessionService sessionService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.sessionService = sessionService;
    }

    @Transactional
    public SessionDTO authenticate(SignInUserDTO userDTO) {
        Optional<User> user = userRepository.findByLogin(userDTO.username());
        if (user.isEmpty()) {
            log.error("user undefined: {}", userDTO.username());
            throw new IllegalArgumentException("User undefined.");
        }
        return sessionService.createNewUserSession(user.get());
    }

    public boolean isUserCredentialsValid(SignInUserDTO userDTO) {
        Optional<User> user = userRepository.findByLogin(userDTO.username());
        if (user.isEmpty()) {
            return false;
        }
        return isPasswordCorrect(userDTO, user.get());
    }

    private boolean isPasswordCorrect(SignInUserDTO userDTO, User user) {
        return passwordService.isPasswordValid(userDTO.password(), user.getPassword());
    }

}