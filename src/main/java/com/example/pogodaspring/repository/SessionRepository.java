package com.example.pogodaspring.repository;

import com.example.pogodaspring.model.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
//todo
@Repository
public class SessionRepository implements BaseRepository<Session, UUID>{
    private final SessionFactory sessionFactory;

    @Autowired
    public SessionRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void saveSession(Session session) {
        sessionFactory.getCurrentSession().persist(session);
    }

    @Transactional
    public void removeSession(Session session) {
        sessionFactory.getCurrentSession().remove(session);
    }

    @Transactional(readOnly = true)
    public Optional<Session> findSessionById(UUID id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Session.class, id));
    }


    @Override
    public void save(Session entity) {

    }

    @Override
    public void remove(Session entity) {

    }

    @Override
    public Optional<Session> findById(UUID uuid) {
        return Optional.empty();
    }
}
