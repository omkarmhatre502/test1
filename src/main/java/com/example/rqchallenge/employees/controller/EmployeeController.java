package com.example.rqchallenge.employees.controller;

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

    @Override
    @GetMapping
    public ResponseEntity<?> getAllEmployees() throws IOException {
        List<Employee> employees = employeeService.getAll();
        return buildSuccessResponse(null, employees, HttpStatus.OK);
    }

    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<?> getEmployeesByNameSearch(@PathVariable String searchString) {
        List<Employee> list = employeeService.getEmployeesByNameSearch(searchString);
        return buildSuccessResponse(null, list, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable String id) {

        try {
            int employeeId = Integer.parseInt(id);
            Employee employee = employeeService.getEmployeeDetails(employeeId);
            if (employee == null) {
                logger.error("[getEmployeeById] Record not found for employeeId %s" + id);
                return buildErrorResponse(new EmployeeException(EmployeeConstants.EMPLOYEE_NOT_FOUND, HttpStatus.NOT_FOUND));
            }
            return buildSuccessResponse(null, employee, HttpStatus.OK);
        } catch (NumberFormatException numberFormatException) {
            return buildErrorResponse(new EmployeeException(EmployeeConstants.INVALID_EMPLOYEE_ID, HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            return buildErrorResponse(new EmployeeException(EmployeeConstants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));
        }

    }

    @Override
    @GetMapping("salary/highest")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        int salary = employeeService.getHighestSalaryOfEmployees();
        return new ResponseEntity<>(salary, HttpStatus.OK);
    }

    @Override
    @GetMapping("salary/top10")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> list = employeeService.getTop10HighestEarningEmployeeNames();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<?> createEmployee(@RequestParam Map<String, Object> employeeInput) {
        try {

            if (!employeeInput.containsKey(EmployeeConstants.NAME) || !employeeInput.containsKey(EmployeeConstants.SALARY) || !employeeInput.containsKey(EmployeeConstants.AGE)) {
                logger.error("[createEmployee] Invalid parameters passed");
                return buildErrorResponse(new EmployeeException(EmployeeConstants.INVALID_PARAM, HttpStatus.BAD_REQUEST));
            }
            Employee input = new Employee();
            input.setEmployeeName(employeeInput.get(EmployeeConstants.NAME).toString());
            input.setEmployeeSalary(Integer.parseInt(employeeInput.get(EmployeeConstants.SALARY).toString()));
            input.setAge(Integer.parseInt(employeeInput.get(EmployeeConstants.AGE).toString()));
            Employee employee = employeeService.createEmployee(input);

            if (employee == null) {
                logger.error("[createEmployee] Failed to create the employee record");
                return buildErrorResponse(new EmployeeException(EmployeeConstants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));
            }
            return buildSuccessResponse(null, employee, HttpStatus.CREATED);
        } catch (NumberFormatException numberFormatException) {
            return buildErrorResponse(new EmployeeException(EmployeeConstants.INVALID_PARAM, HttpStatus.BAD_REQUEST));
        }

    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable String id) {
        try {
            int empId = Integer.parseInt(id);
            if (employeeService.deleteEmployee(empId))
                return buildSuccessResponse(EmployeeConstants.DELETED_RECORD, null, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("[deleteEmployeeById] Exception", e);
        }
        return buildErrorResponse(new EmployeeException("Failed to delete record", HttpStatus.INTERNAL_SERVER_ERROR));

    }

    public ResponseEntity<EmployeeException> buildErrorResponse(EmployeeException employeeException) {
        return new ResponseEntity(employeeException, employeeException.getHttpStatus());
    }

    public ResponseEntity<JsonNode> buildSuccessResponse(String message, Object dataObject, HttpStatus httpStatus) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(EmployeeConstants.STATUS, EmployeeConstants.SUCCESS);
        if (message != null) {
            map.put(EmployeeConstants.MESSAGE, message);
        }
        if (dataObject != null) {
            map.put(EmployeeConstants.DATA, dataObject);
        }
        return new ResponseEntity(map, httpStatus);
    }
}
