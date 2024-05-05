package com.springboot.webflux.client.dao;

import com.springboot.webflux.client.dto.Employee;
import com.springboot.webflux.client.exceptions.ClientDataException;
import com.springboot.webflux.client.exceptions.EmployeeServiceException;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.springboot.webflux.client.constants.EmployeeConstants.*;

@Repository
@Slf4j
public class EmployeeDao {
    private WebClient webClient;

    @Value("${employee.service.base.url}")
    private String baseURl;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.create(baseURl);
    }

    public Flux<Employee> retrieveAllEmployees() {
        return webClient.get().uri(GET_ALL_EMPLOYEES_V1)
                .retrieve()
                .bodyToFlux(Employee.class)
                .onErrorResume(ReadTimeoutException.class, ex -> Mono.error(new ReadTimeoutException(READ_TIME_OUT_EXCEPTION)))
                .onErrorResume(ConnectTimeoutException.class, ex -> Mono.error(new ConnectTimeoutException(CONNECTION_TIME_OUT_EXCEPTION)));
    }

    // Traditional Error Handling
    public Employee retrieveEmployeeById(int id) {
        try {
            return webClient.get().uri(GET_EMPLOYEE_BY_ID, id)
                    .retrieve()
                    .bodyToMono(Employee.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("error response code: {} and error response body: {} ", ex.getStatusCode(), ex.getResponseBodyAsString());
            log.error("webClientResponseException is ", ex);
            throw ex;
        } catch (Exception exception) {
            log.error("exception is ", exception);
            throw exception;
        }
    }

    public Mono<Boolean> checkIfEmployeeExists(int id) {
        return webClient.get().uri(GET_EMPLOYEE_BY_ID, id)
                .retrieve()
                .toBodilessEntity()
                .flatMap(
                        resp -> {
                            HttpStatusCode statusCode = resp.getStatusCode();
                            if (statusCode == HttpStatus.NOT_FOUND) {
                                return Mono.just(false); // Employee not found
                            } else if (statusCode.is2xxSuccessful()) {
                                return Mono.just(true); // Employee found
                            } else {
                                return Mono.error(new EmployeeServiceException("Unexpected error occurred"));
                            }
                        })
                .onErrorResume(ReadTimeoutException.class, ex -> Mono.error(new ReadTimeoutException(READ_TIME_OUT_EXCEPTION)))
                .onErrorResume(ConnectTimeoutException.class, ex -> Mono.error(new ConnectTimeoutException(CONNECTION_TIME_OUT_EXCEPTION)));
    }

    // Functional way of handling error
    // These methods serve different purposes:
    //onStatus is used to handle specific HTTP status codes or ranges of status codes, allowing you to react differently based on the received status.
    //onErrorResume is used to handle errors that occur during the processing of the response, such as network errors or errors thrown during deserialization.
    public Mono<Employee> retrieveEmployeeByIdWithCustomErrorHandling(int id) {
        return webClient.get().uri(GET_EMPLOYEE_BY_ID, id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handle4XXErrors)
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> handle5XXErrors(clientResponse))
                .bodyToMono(Employee.class)
                .retry(3)
                .onErrorResume(ReadTimeoutException.class, ex -> Mono.error(new ReadTimeoutException(READ_TIME_OUT_EXCEPTION)))
                .onErrorResume(ConnectTimeoutException.class, ex -> Mono.error(new ConnectTimeoutException(CONNECTION_TIME_OUT_EXCEPTION)));
    }

    // client errors
    private Mono<? extends Throwable> handle4XXErrors(ClientResponse clientResponse) {
        if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
            return Mono.error(new ClientDataException("Employee not found"));
        } else {
            return clientResponse.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new ClientDataException("Client error: " + body)));
        }
    }

    // server errors
    private Mono<? extends Throwable> handle5XXErrors(ClientResponse clientResponse) {
        Mono<Object> errorMessage = clientResponse.bodyToMono(String.class);
        return errorMessage.flatMap( (message) -> {
            log.error("error response message is " + message);
            throw new EmployeeServiceException((String) message);
        });
    }

    public Mono<Employee> addNewEmployee(Employee employee) {
        return webClient.post().uri(ADD_NEW_EMPLOYEE)
                .bodyValue(employee)
                .retrieve()
                .bodyToMono(Employee.class)
                .onErrorResume(ReadTimeoutException.class, ex -> Mono.error(new ReadTimeoutException(READ_TIME_OUT_EXCEPTION)))
                .onErrorResume(ConnectTimeoutException.class, ex -> Mono.error(new ConnectTimeoutException(CONNECTION_TIME_OUT_EXCEPTION)));
    }
}
