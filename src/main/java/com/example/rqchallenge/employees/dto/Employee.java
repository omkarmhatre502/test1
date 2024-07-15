package com.example.rqchallenge.employees.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "employee_salary")
    private int employeeSalary;

    @Column(name = "employee_age")
    private int age;

    @Column(name = "profile_image")
    private String profileImage;

    public Employee() {

    }

    public Employee(int id, String employeeName, int employeeSalary, int age, String profileImage) {
        this.id = id;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
        this.age = age;
        this.profileImage = profileImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(int employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProfileImage() {
        return profileImage == null ? "" : profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id && employeeName.equals(employee.employeeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeeName);
    }
}
