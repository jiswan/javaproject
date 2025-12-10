package com.mobileparts.puchsystem;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mobileparts.puchsystem.model.Employee;
import com.mobileparts.puchsystem.service.EmployeeService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    public EmployeeService employeeService;
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n");
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("   TESTING EMPLOYEE CRUD OPERATIONS ");
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("\n");
        // ============================================
        // CREATE TEST DATA
        // ============================================
        System.out.println("📝 STEP 1: Creating Test Employees...\n");

        // Assembly Department
        createTestEmployee("Contract",LocalDate.of(2022,11,10),"abc@gmail.com","Assembly","rahman","Abin","EMP005");
        createTestEmployee("Permanent",LocalDate.of(2012,10,1),"efghi@gmail.com","Assembly","jiswan","muhammed","EMP001");
        // Quality Control Department

        // Maintenance Department


        // Production Department

        System.out.println("✅ Created 9 test employees\n");
        System.out.println("─────────────────────────────────────────────────────\n");




        // Employee count
        long count = employeeService.getEmployeeCount();
        System.out.println("✅ Employee Count: " + count + "\n");

        //List all employees
        List<Employee> emp = employeeService.getAllEmployee();
        System.out.println("✅ : " + emp.size() + "Employees\n");
        for (Employee e:emp)
        {
            System.out.println("   - " + emp);
        }
        System.out.println("\n");

        //Findbyid
        Employee empid = employeeService.getEmployeeById("EMP002");
        System.out.println("✅ Found Employee by id : " +empid.getEmployeeId() + " :\n"+empid);

        //deactive
        Employee deactiveEmployee = employeeService.deactivateEmployee("EMP002");
        System.out.println("✅ Employee Deactive: " + deactiveEmployee + "\n");
        List<Employee> updated  = employeeService.getAllEmployee();
        for(Employee e :updated)
        {
            System.out.println("-"+e);
        }
        System.out.println("\n");




    }

    private void createTestEmployee(String employeeType,LocalDate hireDate ,String emailId,String department,String lastName,String firstName,String employeeId)
    {
        Employee e = new Employee(employeeType,hireDate,emailId,department,lastName,firstName,employeeId);
        employeeService.createEmployee(employeeType,hireDate,emailId,department,lastName,firstName,employeeId);
    }
}
