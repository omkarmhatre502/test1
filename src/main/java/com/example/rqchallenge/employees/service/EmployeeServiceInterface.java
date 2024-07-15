package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.dto.Employee;

import java.util.List;

public interface EmployeeServiceInterface {
    public Employee getEmployeeDetails(int employeeId);

    public List<Employee> getAll();

    public Employee createEmployee(Employee employee);

    public boolean deleteEmployee(int employeeId);

    public List<Employee> getEmployeesByNameSearch(String employeeName);

    public int getHighestSalaryOfEmployees();

    public List<String> getTop10HighestEarningEmployeeNames();
}
