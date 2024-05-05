package com.springboot.webflux.client.controller;

import com.springboot.webflux.client.dto.Employee;
import com.springboot.webflux.client.dto.EmployeeErrorResponse;
import com.springboot.webflux.client.exceptions.ClientDataException;
import com.springboot.webflux.client.exceptions.EmployeeServiceException;
import com.springboot.webflux.client.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/allEmployees")
    public Flux<Employee> retrieveAllEmployees() {
        return employeeService.retrieveAllEmployees();
    }


    @GetMapping("/employee/{id}")
    public Mono<Employee> retrieveEmployeeWithID(@PathVariable int id) {
        return employeeService.retrieveEmployeesById(id);
    }

    @PostMapping("/employee")
    public Mono<Employee> addNewEmployee(@RequestBody Employee employee) {
        System.out.println(employee);
        return employeeService.addNewEmployee(employee);
    }
}
