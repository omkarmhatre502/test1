package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.IEmployeeController;
import com.example.rqchallenge.employees.constants.EmployeeConstants;
import com.example.rqchallenge.employees.dto.Employee;
import com.example.rqchallenge.employees.exceptionHadling.EmployeeException;
import com.example.rqchallenge.employees.service.EmployeeService;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController implements IEmployeeController {
    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeException employeeException;

    @Override
    @GetMapping
    public ResponseEntity<?> getAllEmployees() throws IOException {
        List<Employee> employees = employeeService.getAll();
        return buildSuccessResponse(null,employees,HttpStatus.OK);
    }

    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<?> getEmployeesByNameSearch(@PathVariable String searchString) {
        List<Employee> list = employeeService.getEmployeesByNameSearch(searchString);
        return buildSuccessResponse(null,list,HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable String id) {
        int employeeId = Integer.parseInt(id);
        Employee employee = employeeService.getEmployeeDetails(employeeId);
        if(employee == null){
            logger.error("[getEmployeeById] Record not found for employeeId %s",id);
            employeeException.setHttpStatus(HttpStatus.NOT_FOUND);
            employeeException.setMessage(EmployeeConstants.EMPLOYEE_NOT_FOUND);
           return buildErrorResponse(employeeException);
        }

        return buildSuccessResponse(null,employee,HttpStatus.OK);
    }

    @Override
    @GetMapping("salary/high")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        int salary = employeeService.getHighestSalaryOfEmployees();
        return new ResponseEntity<>(salary,HttpStatus.OK);
    }

    @Override
    @GetMapping("salary/high10")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> list = employeeService.getTop10HighestEarningEmployeeNames();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<?> createEmployee(@RequestParam Map<String, Object> employeeInput) {
        Employee employee = employeeService.createEmployee(employeeInput);
        if(employee == null) {
            logger.error("[createEmployee] Invalid parameters passed");
            employeeException.setMessage(EmployeeConstants.INVALID_PARAM);
            employeeException.setHttpStatus(HttpStatus.BAD_REQUEST);
            return buildErrorResponse(employeeException);
        }
        return buildSuccessResponse(null,employee,HttpStatus.CREATED);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable String id) {
        employeeService.deleteEmployee(Integer.parseInt(id));
        return buildSuccessResponse( EmployeeConstants.DELETED_RECORD,null,HttpStatus.OK);
    }

    public ResponseEntity<EmployeeException> buildErrorResponse(EmployeeException employeeException){
        return new ResponseEntity(employeeException,employeeException.getHttpStatus());
    }

    public ResponseEntity<JsonNode> buildSuccessResponse(String message,Object dataObject,HttpStatus httpStatus){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status","success");
        if(message != null) {
            map.put("message", message);
        }
        if(dataObject != null){
            map.put("data",dataObject);
        }
        return new ResponseEntity(map,httpStatus);
    }
}
