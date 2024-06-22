package com.mystore.employees.service;

import com.mystore.employees.Entity.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

@Service
public class EmployeeService {
    List<Employee> employees = new ArrayList<>(Arrays.asList(
            new Employee("Shubham", "Kumar", "92251", "25", "IAG"),
            new Employee("Shivam", "Kumar", "92252", "25", "CAG"),
            new Employee("Vishnu", "Vinoj", "92253", "25", "Iberia"),
            new Employee("Varun", "Singh", "92254", "25", "HCI")
    ));
    public List<Employee> getAllEmployees() {
        return employees;
    }
    public Employee getEmployeeDetails(String id) {
        Employee emp = null;
        for(Employee employee : employees){
            if (employee.getEmployeeId().equals(id)){
                emp = employee;
            }
        }
        return emp;
    }

    public boolean findEmployee (String employeeId){
        for (Employee employee : employees){
            if (employee.getEmployeeId().equals(employeeId))
                return true;
        }
        return false;
    }

    public int createEmployee(Employee employee) {
        String empId = employee.getEmployeeId();
        if (empId.length() > 6){
            return 400;
        }
        int emplId = parseInt(empId);
        if (findEmployee(empId) || emplId < 1000) {
            return 400; // Employee with the same ID already exists
        }

        // Validate first name
        String firstName = employee.getEmployeeFirstName();
        if (!isValidName(firstName)) {
            return 400;
        }

        // Validate last name
        String lastName = employee.getEmployeeLastName();
        if (!isValidName(lastName)) {
            return 400;
        }

        // Validate project name
        String projectName = employee.getEmployeeProject();
        if (!isValidProjectName(projectName)) {
            return 400;
        }

        // If all validations pass, add employee to the collection
        employees.add(employee);
        return 201; // Successful creation
    }

    // Method to validate name (first name and last name)
    private boolean isValidName(String name) {
        String namePattern = "^[A-Za-z]+(?:'[A-Za-z]+)*$"; // Allows alphabets and single apostrophe
        return name.matches(namePattern);
    }

    // Method to validate project name
    private boolean isValidProjectName(String projectName) {
        String projectNamePattern = "^[A-Za-z\\s-]+$"; // Allows alphabets, spaces, and hyphens
        return projectName.matches(projectNamePattern);
    }

    public int updateEmployee (Employee e){
        // Validate first name
        String firstName = e.getEmployeeFirstName();
        if (!isValidName(firstName)) {
            return 400;
        }

        // Validate last name
        String lastName = e.getEmployeeLastName();
        if (!isValidName(lastName)) {
            return 400;
        }

        // Validate project name
        String projectName = e.getEmployeeProject();
        if (!isValidProjectName(projectName)) {
            return 400;
        }
        String id = e.getEmployeeId();
        e.setEmployeeProject(projectName.toUpperCase());
        for(Employee employee : employees){
            if (employee.getEmployeeId().equals(id)){
                employees.remove(employee);
                employees.add(e);
                return 202;
            }
        }
        return  404;
    }

    public int removeEmployee (Employee employee){
        if(findEmployee(employee.getEmployeeId())){
            employees.remove(employee);
            return 204;
        }
        return 404;
    }
}
