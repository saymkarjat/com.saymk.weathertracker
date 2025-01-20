package com.example.pogodaspring.filter;

import com.example.pogodaspring.dto.SessionDTO;
import com.example.pogodaspring.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import java.util.Optional;
import java.util.UUID;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private SessionService sessionService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Optional<SessionDTO> optionalSessionDTO = getSessionDuringIntercept(request);
        if (optionalSessionDTO.isPresent()) {
            SessionDTO session = optionalSessionDTO.get();
            if (!sessionService.isSessionExpired(session)) {
                request.setAttribute("username", session.userLogin());
                request.setAttribute("isUserAuthenticated", true);
                return true;
            } else {
                sessionService.deleteSession(session.id());
            }
        }
        request.setAttribute("isUserAuthenticated", false);
        return true;
    }

    private Optional<SessionDTO> getSessionDuringIntercept(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "session_id");
        if (cookie == null) {
            return Optional.empty();
        }
        String sessionId = cookie.getValue();
        if (sessionId != null) {
            return sessionService.getCurrentSessionByUUID(UUID.fromString(sessionId));
        }
        return Optional.empty();
    }
}