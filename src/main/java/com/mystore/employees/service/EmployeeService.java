package com.mystore.employees.service;

import com.mystore.employees.Entity.Employee;
import com.mystore.employees.database.operations.EmployeeDatabaseActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static java.lang.Integer.parseInt;

@Service
public class EmployeeService extends RuntimeException{
    List<Employee> employees = new ArrayList<>(Arrays.asList(
            new Employee("92251", "Shubham", "Kumar",  "25", "IAG"),
            new Employee("92252", "Shivam", "Kumar",  "25", "CAG"),
            new Employee("92253", "Vishnu", "Vinoj",  "25", "Iberia"),
            new Employee("92254", "Varun", "Singh",  "25", "HCI")
    ));

    @Autowired
    EmployeeDatabaseActions employeeDatabase;

    public List<Employee> getAllEmployees() {
        return employeeDatabase.findAll();
    }

    public Employee getEmployeeDetails(String id)  throws NullPointerException{
        List<Employee> employees  = employeeDatabase.findAll();
        for (Employee employee : employees) {
            if (employee.getEmployeeId().equals(id))
                return employee;
        }
        return null;
    }

    public boolean findEmployee (String employeeId){
          List<Employee> employees  = employeeDatabase.findAll();
          for (Employee employee : employees) {
              if (employee.getEmployeeId().equals(employeeId))
                  return true;
          }
          return false;
    }

    public void logMessage(String message) {
        LocalDateTime now = LocalDateTime.now();
        ZoneId istZoneId = ZoneId.of("Asia/Kolkata");
        ZonedDateTime istZonedDateTime = now.atZone(istZoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String formattedDate = istZonedDateTime.format(formatter);
        System.out.println(formattedDate + " " + message);
    }

    public int createEmployee(Employee employee) {
        String empId = employee.getEmployeeId();
        if (empId.length() > 6){
            logMessage("Employee ID is not in expected format");
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
        //employees.add(employee);
        employeeDatabase.save(employee);
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
        if (findEmployee(id)) {
            employeeDatabase.save(e);
            return 202;
        }
        return  404;
    }

    public int removeEmployee (Employee employee){
        if(findEmployee(employee.getEmployeeId())){
            employeeDatabase.delete(employee);
            return 204;
        }
        return 404;
    }
}
