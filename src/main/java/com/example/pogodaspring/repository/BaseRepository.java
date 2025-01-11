package com.example.pogodaspring.repository;

import java.util.Optional;

public interface BaseRepository<T, ID> {
    void save(T entity);
    void remove(T entity);
    Optional<T> findById(ID id);
}