//package com.springboot.webflux.code.dao;
//
//import com.springboot.webflux.code.dto.Employee;
//import com.springboot.webflux.code.exceptions.ClientDataException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//class EmployeeDaoTest {
//
//    private static final String BASEURL = "http://localhost:8081/employeeservice/";
//    private final WebClient webClient = WebClient.create(BASEURL);
////    EmployeeDao employeeDao = new EmployeeDao(webClient);
//
//    @Test
//    void retrieveAllEmployeesTest() {
//        List<Employee> employeeList = employeeDao.retrieveAllEmployees();
//        System.out.println(employeeList);
//        assertFalse(employeeList.isEmpty());
//    }
//
//    @Test
//    void retrieveEmployeeByIdTest() {
//        Employee employee = employeeDao.retrieveEmployeeById(1);
//        assertEquals("Chris", employee.getFirstName());
//        System.out.println(employee);
//    }
//
//    @Test
//    void retrieveEmployeeByInvalidIdTest() {
//        Assertions.assertThrows(WebClientResponseException.class, () -> employeeDao.retrieveEmployeeById(55));
//    }
//
//
//    @Test
//    void retrieveEmployeeByIdWithCustomErrorHandlingInvalidId() {
//        Assertions.assertThrows(ClientDataException.class, () -> employeeDao.retrieveEmployeeByIdWithCustomErrorHandling(55));
//    }
//
//    @Test
//    void retrieveEmployeeByIdWithCustomErrorHandlingValidId() throws InterruptedException {
//        Mono<Employee> employee = employeeDao.retrieveEmployeeByIdWithCustomErrorHandling(1);
////        Thread.sleep(1000);
////        employee.subscribe(
////                response -> System.out.println("Employee Retrieved: " + response.getFirstName()),
////                error -> System.err.println("Error occurred: " + error),
////                () -> System.out.println("Employee retrieval completed.")
////        );
//
////        Thread.sleep(2000);
////        StepVerifier.create(employee)
////                .expectNextCount(1) // Ensure only one employee is retrieved
////                .verifyComplete();
//    }
//
//
//    @Test
//    void addNewEmployee() {
//        Employee employee = new Employee(45, "Vivek", "Male", 10, "Sumanth", "freelancer");
//        employeeDao.addNewEmployee(employee);
//        System.out.println(employee);
//    }
//}