package com.springboot.webflux.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeErrorResponse {
    private HttpStatus status;
    private String message;
    private long timeStamp;

}
