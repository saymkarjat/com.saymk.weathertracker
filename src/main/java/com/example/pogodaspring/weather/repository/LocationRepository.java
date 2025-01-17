package com.example.pogodaspring.weather.repository;

import com.example.pogodaspring.model.Location;
import com.example.pogodaspring.model.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

}
