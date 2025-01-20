package com.example.pogodaspring.service;

import com.example.pogodaspring.dto.SessionDTO;
import com.example.pogodaspring.dto.SessionMapper;
import com.example.pogodaspring.model.Session;
import com.example.pogodaspring.model.User;
import com.example.pogodaspring.repository.SessionRepository;
import com.example.pogodaspring.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;


    public SessionService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SessionDTO createNewUserSession(User user) {
        Session session = new Session();
        session.setExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS));
        session.setUser(user);
        sessionRepository.saveSession(session);
        return SessionMapper.INSTANCE.sessionToSessionDTO(session);
    }

    @Transactional
    public void deleteSession(UUID sessionId) {
        Optional<Session> session = sessionRepository.findSessionById(sessionId);
        if (session.isEmpty()) {
            throw new IllegalArgumentException("Session undefined");
        }
        sessionRepository.removeSession(session.get());
    }


    public Optional<SessionDTO> getCurrentSessionByUUID(UUID id) {
        Optional<Session> sessionOptional = sessionRepository.findSessionById(id);
        if (sessionOptional.isEmpty()) {
            return Optional.empty();
        }
        Session session = sessionOptional.get();
        return Optional.ofNullable(SessionMapper.INSTANCE.sessionToSessionDTO(session));
    }


    public boolean isSessionExpired(SessionDTO sessionDTO) {
        return !sessionDTO.expiresAt().isAfter(Instant.now());
    }


}
