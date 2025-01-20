package com.example.pogodaspring.repository;

import com.example.pogodaspring.model.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class SessionRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public SessionRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveSession(Session session) {
        sessionFactory.getCurrentSession().persist(session);
    }

    public void removeSession(Session session) {
        sessionFactory.getCurrentSession().remove(session);
    }

    @Transactional(readOnly = true)
    public Optional<Session> findSessionById(UUID id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Session.class, id));
    }

    public int deleteExpiredSessions() {
        String hql = "DELETE FROM Session WHERE expiresAt < :now";
        return sessionFactory.getCurrentSession().createQuery(hql, Session.class)
                .setParameter("now", Instant.now())
                .executeUpdate();
    }

}
