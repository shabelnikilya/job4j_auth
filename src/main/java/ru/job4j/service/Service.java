package ru.job4j.service;

import ru.job4j.domain.Person;

import java.util.Optional;

public interface Service {

    Iterable<Person> findAll();

    Optional<Person> findById(int id);

    Person save(Person person);

    void deletePerson(Person person);
}
