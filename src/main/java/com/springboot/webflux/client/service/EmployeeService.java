package com.springboot.webflux.client.service;

import com.springboot.webflux.client.dao.EmployeeDao;
import com.springboot.webflux.client.dto.Employee;
import com.springboot.webflux.client.exceptions.EmployeeServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    public Flux<Employee> retrieveAllEmployees() {
        return employeeDao.retrieveAllEmployees();
    }

    public Mono<Employee> retrieveEmployeesById(int id) {
        return employeeDao.retrieveEmployeeByIdWithCustomErrorHandling(id);
    }

    public Mono<Employee> addNewEmployee(Employee employee) {
        // check if employee with id exists
        // if exists return employee already exists error.
        // if not then add the employee
        return employeeDao.checkIfEmployeeExists(employee.getId())
                .flatMap(checkIfExists -> {
                    if (checkIfExists) {
                        // Employee already exists, throw an exception
                        return Mono.error(new EmployeeServiceException("Employee with ID " + employee.getId() + " already exists."));
                    } else {
                        // Employee doesn't exist, proceed with adding
                        return employeeDao.addNewEmployee(employee);
                    }
                });
    }
}
