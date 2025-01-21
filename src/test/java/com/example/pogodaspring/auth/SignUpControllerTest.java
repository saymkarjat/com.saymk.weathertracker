package com.example.pogodaspring.auth;
import com.example.pogodaspring.config.TestSpringConfig;
import com.example.pogodaspring.model.User;
import com.example.pogodaspring.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig(TestSpringConfig.class)
@ActiveProfiles("test")
class SignUpControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SignUpController signUpController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(signUpController).build();

    }
    @Test
    @SneakyThrows
    void shouldRedirectAndSaveUserWhenSignUpIsSuccessful() {
        mockMvc.perform(post("/auth/signup")
                        .param("username", "cherv")
                        .param("password", "12345678")
                        .param("confirmPassword", "12345678"))
                .andExpect(status().is3xxRedirection()
                );

        assertTrue(userRepository.findByLogin("cherv").isPresent());
    }

    @Test
    @SneakyThrows
    void shouldThrowExceptionIfUserWithThisLoginAlreadyExist() {
        User user = new User();
        user.setLogin("cherv1");
        user.setPassword("12345678");
        userRepository.saveUser(user);
        mockMvc.perform(post("/auth/signup")
                        .param("username", "cherv1")
                        .param("password", "12345678")
                        .param("confirmPassword", "12345678"))
                .andExpect(status().isOk())
                .andExpect(view().name("signuppost"))
                .andExpect(model().attributeHasFieldErrorCode("userDTO", "username", "error.username"));
    }

}