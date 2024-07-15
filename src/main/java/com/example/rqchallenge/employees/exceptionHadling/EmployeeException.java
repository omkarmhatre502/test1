package com.example.rqchallenge.employees.exceptionHadling;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class EmployeeException {
    private String message;
    private HttpStatus httpStatus;

    public EmployeeException() {

    }

    public EmployeeException(String message, HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return "EmployeeException{" +
                "message='" + message + '\'' +
                ", httpStatus=" + httpStatus +
                '}';
    }
}
