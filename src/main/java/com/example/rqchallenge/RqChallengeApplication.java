package com.example.rqchallenge;

import com.example.rqchallenge.employees.controller.EmployeeController;
import com.example.rqchallenge.employees.dto.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RqChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RqChallengeApplication.class, args);

    }

}
