package com.example.pogodaspring.service;

import com.example.pogodaspring.dto.SignUpUserDTO;
import com.example.pogodaspring.exception.UserAlreadyExistException;
import com.example.pogodaspring.model.User;
import com.example.pogodaspring.repository.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignUpService {
    //todo
    private UserRepository userRepository;
    private PasswordService passwordService;

    @Autowired
    public SignUpService(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Transactional
    public void signUp(SignUpUserDTO userDTO) {
        User user = User.builder()
                .login(userDTO.username())
                .password(passwordService.encodePassword(userDTO.password()))
                .build();
        try {
            userRepository.saveUser(user);
        } catch (ConstraintViolationException e) {
            throw new UserAlreadyExistException("This username is already taken");
        }
    }
}
