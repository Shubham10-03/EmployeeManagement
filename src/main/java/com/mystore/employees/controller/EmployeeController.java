package com.mystore.employees.controller;

import com.mystore.employees.Entity.Employee;
import com.mystore.employees.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
public class EmployeeController {

    @Autowired
    EmployeeService employees;

    @GetMapping("/employees")
    public List<Employee> findAllEmployees() {
        return employees.getAllEmployees();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        Employee employee = employees.getEmployeeDetails(id);
        if (employee != null) {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/employees")
    public ResponseEntity<Void> createNewEmployee(@RequestBody Employee employee) {
        int responseCode = employees.createEmployee(employee);
        if (responseCode == 201)
            return new ResponseEntity<>(HttpStatus.CREATED);
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/employees")
    public ResponseEntity<Void> updateEmployeeDetails(@RequestBody Employee employee) {
        int responseCode = employees.updateEmployee(employee);
        if (responseCode == 202) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else if (responseCode == 400){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("employees/{id}")
    public ResponseEntity<Void> removeEmployeeDetails (@PathVariable String id){
        Employee employee = employees.getEmployeeDetails(id);
        int responseCode = employees.removeEmployee(employee);
        if (responseCode == 204) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
