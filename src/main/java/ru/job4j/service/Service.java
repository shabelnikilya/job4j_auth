package ru.job4j.service;

import ru.job4j.domain.Person;

import java.util.Optional;

public interface Service<T> {

    Iterable<T> findAll();

    Optional<T> findById(int id);

    T save(T t);

    void deletePerson(T t);
}
