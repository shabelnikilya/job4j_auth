package ru.job4j.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.domain.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    @Override
    @Query("select distinct e from Employee e join fetch e.accounts")
    Iterable<Employee> findAll();
}
