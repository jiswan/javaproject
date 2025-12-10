package com.mobileparts.puchsystem.repository;

import com.mobileparts.puchsystem.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,String> {
    Optional<Employee> findByEmailId(String emailId);
    List<Employee> findByDepartment(String department);
    List<Employee> findByIsActiveTrue();
    Optional<Employee> findByFirstNameAndLastName(String fName,String lName);

}
