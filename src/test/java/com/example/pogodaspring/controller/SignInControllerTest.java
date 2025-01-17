package com.example.pogodaspring.controller;

import com.example.pogodaspring.config.TestSpringConfig;
import com.example.pogodaspring.model.Session;
import com.example.pogodaspring.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ActiveProfiles("test")
@SpringJUnitConfig(classes = TestSpringConfig.class)
@WebAppConfiguration
class SignInControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @SneakyThrows
    void signIn() {
        mockMvc.perform(post("/auth/signup")
                        .param("username", "vasya1")
                        .param("password", "12345678")
                        .param("confirmPassword", "12345678"))
                .andExpect(status().is3xxRedirection());


        mockMvc.perform(post("/auth/signin")
                        .param("username", "vasya1")
                        .param("password", "12345678"))
                        .andExpect(status()
                        .is3xxRedirection());
        Session session = userRepository.getUserSessions("vasya1").get(0);
        assertTrue(session.getExpiresAt().isAfter(Instant.now().plus(119, ChronoUnit.MINUTES)));
    }

    @Test
    @SneakyThrows
    void logOut() {
        mockMvc.perform(post("/auth/signup")
                        .param("username", "vasya")
                        .param("password", "12345678")
                        .param("confirmPassword", "12345678"))
                .andExpect(status().is3xxRedirection());


        MvcResult result = mockMvc.perform(post("/auth/signin")
                        .param("username", "vasya")
                        .param("password", "12345678"))
                .andExpect(status()
                        .is3xxRedirection()).andReturn();
        Cookie sessionCookie = result.getResponse().getCookie("session_id");

        mockMvc.perform(get("/auth/logout").cookie(sessionCookie))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/auth/logout")).andExpect(status().is3xxRedirection());
        List<Session> vasya = userRepository.getUserSessions("vasya");
        assertTrue(vasya.isEmpty());
    }
}