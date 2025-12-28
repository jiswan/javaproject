package com.mobileparts.puchsystem.Controller;

import com.mobileparts.puchsystem.model.Employee;
import com.mobileparts.puchsystem.repository.EmployeeRepository;
import com.mobileparts.puchsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    public EmployeeService employeeService;

    @Autowired
    public EmployeeRepository employeeRepository;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployee()
    {
       List<Employee> employees = employeeService.getAllEmployee();
       return ResponseEntity.ok(employees);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String employeeId)
    {
        Optional<Employee> employee = employeeService.getEmployeeByEmployeeId(employeeId);
        if (employee.isPresent())
        {
            return ResponseEntity.ok(employee.get());
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public  ResponseEntity<Employee> createEmployee(@RequestBody Employee employee)
    {
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }
    @PutMapping("/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String employeeId, @RequestBody Employee employeeDetails)
    {
        Optional<Employee> employeeOpt = employeeService.getEmployeeByEmployeeId(employeeId);
        if(!employeeOpt.isPresent())
        {
            return ResponseEntity.notFound().build();
        }

        Employee existingEmployee = employeeOpt.get();
        if(employeeDetails.getFirstName()!=null)
        {
            existingEmployee.setFirstName(employeeDetails.getFirstName());
        }
        if (employeeDetails.getLastName() != null) {
            existingEmployee.setLastName(employeeDetails.getLastName());
        }

        if (employeeDetails.getEmailId() != null) {
            existingEmployee.setEmailId(employeeDetails.getEmailId());
        }

        if (employeeDetails.getDepartment() != null) {
            existingEmployee.setDepartment(employeeDetails.getDepartment());
        }

        Employee updatedEmployee = employeeService.saveEmployee(existingEmployee);
        return  ResponseEntity.ok(updatedEmployee);


    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable String employeeId)
    {
        Optional<Employee> employeeOpt = employeeService.getEmployeeByEmployeeId(employeeId);
        if (!employeeOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Employee deleteEmployee = employeeOpt.get();
        employeeRepository.delete(deleteEmployee);
        return ResponseEntity.noContent().build();
    }

}
