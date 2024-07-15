package com.example.rqchallenge.employees.repo;

import com.example.rqchallenge.employees.dto.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    @Query(value = "SELECT * FROM employee e WHERE employee_name like %?1%", nativeQuery = true)
    List<Employee> findByEmployeeNameLike(String name);

    @Query(value = "SELECT employee_salary FROM employee order by employee_salary DESC limit 1 ", nativeQuery = true)
    Integer findHighestEmployeeSalary();

    @Query(value = "SELECT employee_name FROM employee order by employee_salary DESC limit 10 ", nativeQuery = true)
    List<String> findTop10HighestEarningEmployeeNames();

}
