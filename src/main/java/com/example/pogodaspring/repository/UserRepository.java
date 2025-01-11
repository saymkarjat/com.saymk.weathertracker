package com.example.pogodaspring.repository;

import com.example.pogodaspring.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
//todo
@Repository
public class UserRepository implements BaseRepository<User, Long> {
    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void saveUser(User user) {
        sessionFactory.getCurrentSession().persist(user);
    }

    public void updateUser(User user) {
        sessionFactory.getCurrentSession().merge(user);
    }

    @Transactional
    public void removeUser(User user) {
        sessionFactory.getCurrentSession().remove(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(User.class, id));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByLogin(String login) {
        return sessionFactory.getCurrentSession().createQuery("from User WHERE login =:login", User.class)
                .setParameter("login", login)
                .uniqueResultOptional();
    }




    @Override
    public void save(User entity) {

    }

    @Override
    public void remove(User entity) {

    }

    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.empty();
    }
    // реализация методов UserRepository
}
