package com.mystore.employees.database.operations;

import com.mystore.employees.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeDatabaseActions extends JpaRepository<Employee, String> {

}
