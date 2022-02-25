package ru.job4j.service;


import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;

import java.util.Optional;

@org.springframework.stereotype.Service
public class PersonService implements Service {
    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<Person> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Person> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public Person save(Person person) {
        return repository.save(person);
    }

    @Override
    public void deletePerson(Person person) {
        repository.delete(person);
    }
}
