package com.example.pogodaspring.service;

import com.example.pogodaspring.dto.SessionDTO;
import com.example.pogodaspring.dto.SessionMapper;
import com.example.pogodaspring.model.Session;
import com.example.pogodaspring.model.User;
import com.example.pogodaspring.repository.SessionRepository;
import com.example.pogodaspring.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {
    //todo baserepository
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;


    public SessionService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SessionDTO createNewUserSession(User user) {
        Session session = new Session();
        session.setExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS));
        session.setUser(user);

        sessionRepository.saveSession(session);
        user.getSessions().add(session);
        userRepository.updateUser(user);

        return SessionMapper.INSTANCE.sessionToSessionDTO(session);
    }

    @Transactional
    public void deleteSession(UUID sessionId) {
        Optional<Session> session = sessionRepository.findSessionById(sessionId);
        if (session.isEmpty()) {
            throw new IllegalArgumentException("Session undefined");
        }
        User user = session.get().getUser();
        user.getSessions().remove(session.get());
        //sessionRepository.removeSession(session.get());

        userRepository.updateUser(user);
    }

    public void injectCookieIntoResponse(HttpServletResponse resp, UUID id) {
        Cookie cookie = new Cookie("session_id", id.toString());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(2 * 3600);

        resp.addCookie(cookie);
    }

    public Optional<SessionDTO> getCurrentSessionByUUID(UUID id) {
        Optional<Session> sessionOptional = sessionRepository.findSessionById(id);
        if (sessionOptional.isEmpty()) {
            return Optional.empty();
        }
        Session session = sessionOptional.get();
        return Optional.ofNullable(SessionMapper.INSTANCE.sessionToSessionDTO(session));
    }

    public Optional<SessionDTO> getSessionDuringIntercept(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "session_id");
        if (cookie == null) {
            return Optional.empty();
        }
        String sessionId = cookie.getValue();
        if (sessionId != null) {
            return getCurrentSessionByUUID(UUID.fromString(sessionId));
        }
        return Optional.empty();
    }

    public boolean isSessionExpired(SessionDTO sessionDTO) {
        return !sessionDTO.expiresAt().isAfter(Instant.now());
    }

    @Transactional
    public void deleteSessionIfNewUserHasBeenRegistered(HttpServletRequest req, HttpServletResponse resp) {
        Cookie cookie = WebUtils.getCookie(req, "session_id");
        if (cookie != null) {
            try {
                deleteSession(UUID.fromString(cookie.getValue()));
            } catch (IllegalArgumentException e) {
                //log.info
            }
            cookie.setValue("");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            resp.addCookie(cookie);
        }
    }
}
