package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.constants.EmployeeConstants;
import com.example.rqchallenge.employees.dto.Employee;
import com.example.rqchallenge.employees.repo.EmployeeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService  {
    Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    EmployeeRepo employeeRepo;

    public Employee getEmployeeDetails(int empId) {
        return employeeRepo.findById(empId).orElse(null);
    }

    public List<Employee> getAll(){
        List<Employee> list = employeeRepo.findAll();
        list = list == null ? Collections.emptyList() : list;
        return list;
    }

    public Employee createEmployee(Map<String, Object> employeeInput) {
        Employee employee = new Employee();
        if(!employeeInput.containsKey(EmployeeConstants.NAME) || !employeeInput.containsKey(EmployeeConstants.SALARY) || !employeeInput.containsKey(EmployeeConstants.AGE))
        {
            logger.error("Invalid parameters passed");
          return null;
        }
        employee.setEmployeeName(employeeInput.get(EmployeeConstants.NAME).toString());
        employee.setEmployeeSalary(Integer.parseInt(employeeInput.get(EmployeeConstants.SALARY).toString()));
        employee.setAge(Integer.parseInt(employeeInput.get(EmployeeConstants.AGE).toString()));
        return employeeRepo.save(employee);
    }

    public void deleteEmployee(int id){
        if(getEmployeeDetails(id) != null) {
            employeeRepo.deleteById(id);
            logger.info("[deleteEmployee] Successfully deleted employee with record id= "+id);
        }
        else{
            logger.error("[deleteEmployee] Invalid parameters passed id= "+id);
        }
    }

    public List<Employee> getEmployeesByNameSearch(String name){
        List<Employee> list = employeeRepo.findByEmployeeNameLike(name);
        return list==null ? Collections.emptyList() : list;
    }

    public int getHighestSalaryOfEmployees(){
        return employeeRepo.findHighestEmployeeSalary();
    }

    public List<String> getTop10HighestEarningEmployeeNames(){
        return employeeRepo.findTop10HighestEarningEmployeeNames();
    }
}
