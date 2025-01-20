package com.example.pogodaspring.service;

import com.example.pogodaspring.repository.SessionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class SessionCleanupService {
    private SessionRepository sessionRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanUpExpiredSessions() {
        int deleted = sessionRepository.deleteExpiredSessions();
        if (deleted > 0) {
            log.info("Successfully deleted {} expired sessions.", deleted);
        } else {
            log.info("No expired sessions to delete.");
        }
    }
}
