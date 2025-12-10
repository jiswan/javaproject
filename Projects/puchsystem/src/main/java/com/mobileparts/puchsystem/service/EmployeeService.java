package com.mobileparts.puchsystem.service;

import com.mobileparts.puchsystem.model.Employee;
import com.mobileparts.puchsystem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeService
{
    @Autowired
    public EmployeeRepository employeeRepository;

    public Employee createEmployee (String employeeType, LocalDate hireDate, String emailId, String department, String lastName, String firstName, String employeeId)
    {
        Employee employee = new Employee(employeeType,hireDate,emailId,department,lastName,firstName,employeeId);
        Employee savedEmployee = employeeRepository.save(employee);
        System.out.println("Employee created: " + savedEmployee);
        return savedEmployee;

    }

    public Employee getEmployeeById(String employeeId)
    {
        return  employeeRepository.findById(employeeId)
                .orElseThrow(()->new RuntimeException("Employee not found") );
    }

    public List<Employee> getAllEmployee()
    {
        return employeeRepository.findAll();
    }

    public long getEmployeeCount()
    {
        return employeeRepository.count();
    }
    public Employee updateEmployeeDepartment(String employeeId, String newDepartment)
    {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()->new RuntimeException("Employee not found"));
        System.out.println("Before update: " + employee);
        employee.setDepartment(newDepartment);
        Employee updatedEmployee = employeeRepository.save(employee);
        System.out.println("Updated Employee Department: " + updatedEmployee);
        return updatedEmployee;

    }

    public Employee updateEmployeeEmail(String employeeId,String updateEmail)
    {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()->new RuntimeException("Employee not found "));
        employee.setEmailId(updateEmail);
        return employeeRepository.save(employee);
    }

    //deactivate
    public Employee deactivateEmployee( String employeeId)
    {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()->new RuntimeException("Employee not found"));
        employee.setActive(false);
        return  employeeRepository.save(employee);
    }
    //findByEmail
    public Employee getByEmailId( String emailId1)
    {
        return employeeRepository.findByEmailId(emailId1)
                .orElseThrow(()-> new RuntimeException("Employee not found"));
    }
    //employee information by department
    public List<Employee> getByDepartment( String department)
    {
        return employeeRepository.findByDepartment(department);
    }
    //number of employee active
    public List<Employee> getEmployeeByActive()
    {
        return employeeRepository.findByIsActiveTrue();
    }

    //Employee Full Name
    public  Employee getFullName(String fName,String lName)
    {
        return  employeeRepository.findByFirstNameAndLastName(fName,lName)
                .orElseThrow(()->new RuntimeException("Not found"));

    }







}
