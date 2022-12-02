package com.example;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer {

    @Autowired
    private EmployeeRepository repository;

    @PostConstruct
    void postConstruct() {
        repository.saveAll(createEmployees());

    }

    private List<Employee> createEmployees() {
        String[] departments = {"IT", "Sales", "Admin", "Account"};
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            employees.add(Employee.create(RandomUtil.getName(),
                    RandomUtil.getAnyOf(departments),
                    RandomUtil.getInt(1, 10) * 1000));
        }
        return employees;
    }
}
