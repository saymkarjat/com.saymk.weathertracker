package com.example.pogodaspring.weather.repository;

import com.example.pogodaspring.model.Location;
import com.example.pogodaspring.model.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LocationRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public LocationRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void saveLocation(Location location) {
        sessionFactory.getCurrentSession().persist(location);
    }

    @Transactional
    public void removeLocation(Location location) {
        sessionFactory.getCurrentSession().remove(location);
    }

    @Transactional(readOnly = true)
    public Optional<Location> findLocationById(int id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Location.class, id));
    }

    @Transactional(readOnly = true)
    public List<Location> getUserLocations(String username){
        String hql = "SELECT l FROM Location l JOIN FETCH l.user u WHERE u.login = :login";
        return sessionFactory.getCurrentSession().createQuery(hql, Location.class)
                .setParameter("login", username)
                .getResultList();
    }
    @Transactional(readOnly = true)
    public List<Location> getRequiredList(int size, int page, String username) {
        return sessionFactory.getCurrentSession().createQuery("select l from Location l where l.user.login = :username", Location.class)
                .setParameter("username", username)
                .setFirstResult(size*page)
                .setMaxResults(size)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public long countLocationsByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("select count(l) from Location l where l.user.login = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
    }

}
