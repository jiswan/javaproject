package com.mobileparts.puchsystem.Controller;

import com.mobileparts.puchsystem.model.Employee;
import com.mobileparts.puchsystem.repository.EmployeeRepository;
import com.mobileparts.puchsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.server.RemoteRef;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Employee employee = employeeService.getEmployeeByEmployeeId(employeeId);
       return ResponseEntity.ok(employee);
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
        Employee existingEmployee = employeeService.getEmployeeByEmployeeId(employeeId);

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
        Employee employeeOpt = employeeService.getEmployeeByEmployeeId(employeeId);
        employeeRepository.delete(employeeOpt);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployee(@RequestParam String query)
    {
        String searchLower = query.toLowerCase();
        List<Employee> allEmployees = employeeRepository.findAll();
        List<Employee> result = allEmployees.stream().filter(
                e-> e.getFirstName().toLowerCase().contains(searchLower)||
                        e.getLastName().toLowerCase().contains(searchLower)||
                        e.getFullName().toLowerCase().contains(searchLower)
        ).toList();

        return  ResponseEntity.ok(result);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Employee>> filterEmployee(@RequestParam(required = false) String department,
                                                         @RequestParam(required = false) String type)
    {
        List<Employee> employee = employeeRepository.findAll();
        if(department!=null && !department.isEmpty())
        {
            employee = employee.stream().filter(e->e.getDepartment().equalsIgnoreCase(department)).toList();
        }

        if(type!=null&& !type.isEmpty())
        {
            employee = employee.stream().filter(
                    e->e.getEmployeeType().equalsIgnoreCase(type)).toList();
        }
        return  ResponseEntity.ok(employee);
    }

    @GetMapping("/Employee/{department}")
    public ResponseEntity<List<Employee>> getByDepoartment(@PathVariable String department)
    {
        List<Employee> result = employeeRepository.findByDepartment(department);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/active")
    public ResponseEntity<List<Employee>> getActiveEmployee()
    {
        List<Employee> activeEmployee = employeeRepository.findByIsActiveTrue();
        return ResponseEntity.ok(activeEmployee);
    }

}
