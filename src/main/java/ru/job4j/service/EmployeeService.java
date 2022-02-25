package ru.job4j.service;

import ru.job4j.domain.Employee;
import ru.job4j.repository.EmployeeRepository;

import java.util.Optional;

@org.springframework.stereotype.Service
public class EmployeeService implements Service<Employee> {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<Employee> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Employee> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public void deletePerson(Employee employee) {
        repository.delete(employee);
    }
}
