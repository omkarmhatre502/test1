package com.example.rqchallenge.controller;


import com.example.rqchallenge.employees.constants.EmployeeConstants;
import com.example.rqchallenge.employees.controller.EmployeeController;
import com.example.rqchallenge.employees.dto.Employee;
import com.example.rqchallenge.employees.exceptionHadling.EmployeeException;
import com.example.rqchallenge.employees.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @MockBean
    EmployeeService employeeService;

    @MockBean
    EmployeeException employeeException;

    @Autowired
    MockMvc mockMvc;


    @BeforeEach
    void setup() {
        Employee emp1 = new Employee(1, "emp1", 500, 20, "");
        Employee emp2 = new Employee(2, "em2", 5000, 27, "");
        Employee emp3 = new Employee(2, "emp3", 8008, 37, "");

        List<Employee> employees = new ArrayList<>() {
        };
        employees.add(emp1);
        employees.add(emp3);

        when(employeeService.getAll()).thenReturn(employees);
        when(employeeService.getEmployeeDetails(1)).thenReturn(emp1);
        when(employeeService.getEmployeesByNameSearch("emp")).thenReturn(employees);
        Employee employee = new Employee();
        employee.setEmployeeName("emp20");
        employee.setEmployeeSalary(500);
        employee.setAge(20);
        when(employeeService.createEmployee(employee)).thenReturn(emp1);

    }

    @Test
    public void getAllEmployeesTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.
                get("/api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray());

    }

    @Test
    public void getEmployeeByIdTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.
                get("/api/v1/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.employeeName").value("emp1"));

    }

    @Test
    public void getEmployeesByNameSearchTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.
                get("/api/v1/employee/search/emp")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].employeeName").value("emp1"));

    }

    @Test
    public void createEmployeeTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.
                post("/api/v1/employee/create")
                .contentType(MediaType.APPLICATION_JSON)
                .param(EmployeeConstants.NAME, "emp20")
                .param(EmployeeConstants.SALARY, "500")
                .param(EmployeeConstants.AGE, "20")
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void createEmployeeNegativeTest() throws Exception {
        when(employeeException.getHttpStatus()).thenReturn(HttpStatus.BAD_REQUEST);
        mockMvc.perform(MockMvcRequestBuilders.
                post("/api/v1/employee/create")
                .contentType(MediaType.APPLICATION_JSON)
                .param(EmployeeConstants.NAME, "emp20")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deleteEmployeeTest() throws Exception {
        when(employeeService.deleteEmployee(1)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.
                delete("/api/v1/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
