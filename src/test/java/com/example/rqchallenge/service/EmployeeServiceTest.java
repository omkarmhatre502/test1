package com.example.rqchallenge.service;

import com.example.rqchallenge.employees.dto.Employee;
import com.example.rqchallenge.employees.exceptionHadling.EmployeeException;
import com.example.rqchallenge.employees.repo.EmployeeRepo;
import com.example.rqchallenge.employees.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class EmployeeServiceTest {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeException employeeException;

    @MockBean
    EmployeeRepo employeeRepo;

    Employee employee = new Employee();

    @BeforeEach
    void setup() {
        Employee emp1 = new Employee(1, "emp1", 500, 20, "");
        Employee emp2 = new Employee(2, "em2", 5000, 27, "");
        Employee emp3 = new Employee(2, "emp3", 8008, 37, "");

        Mockito.when(employeeRepo.findAll()).thenReturn(Stream.
                of(emp1, emp2).collect(Collectors.toList()));

        Mockito.when(employeeRepo.findById(1)).thenReturn(Optional.of(emp1));

        Mockito.when(employeeRepo.findByEmployeeNameLike("emp")).thenReturn(Stream.
                of(emp1, emp3).collect(Collectors.toList()));

        Mockito.when(employeeRepo.findHighestEmployeeSalary()).thenReturn(emp3.getEmployeeSalary());


        employee.setEmployeeName(emp1.getEmployeeName());
        employee.setEmployeeSalary(emp1.getEmployeeSalary());
        employee.setAge(emp1.getAge());

        Mockito.when(employeeRepo.save(employee)).thenReturn(emp1);


    }

    @Test
    void getAllEmployeeTest() {
        assertEquals(2, employeeService.getAll().size());
    }

    @Test
    void getEmployeeByIdTest() {
        assertEquals("emp1", employeeService.getEmployeeDetails(1).getEmployeeName());
    }

    @Test
    void getEmployeeByNameTest() {
        assertEquals(2, employeeService.getEmployeesByNameSearch("emp").size());
    }

    @Test
    void getHighestSalaryOfEmployeesTest() {
        assertEquals(8008, employeeService.getHighestSalaryOfEmployees());
    }

    @Test
    void getTop10HighestEarningEmployeeNamesTest() {
        Mockito.when(employeeRepo.findTop10HighestEarningEmployeeNames()).thenReturn(Stream.
                of("emp1", "emp2").collect(Collectors.toList()));
        assertEquals(true, employeeService.getTop10HighestEarningEmployeeNames().contains("emp1"));
        assertEquals(2, employeeService.getTop10HighestEarningEmployeeNames().size());
    }

    @Test
    void createEmployeeNegativeTest() {
        Employee invalidEmployee = new Employee();
        employee.setEmployeeName("emp5");
        assertEquals(null, employeeService.createEmployee(invalidEmployee));
    }

    @Test
    void createEmployeePositiveTest() {

        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(1, employeeResult.getId());
        assertEquals("emp1", employeeResult.getEmployeeName());
        assertEquals(500, employeeResult.getEmployeeSalary());
        assertEquals(20, employeeResult.getAge());

    }
}
