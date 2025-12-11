package com.mobileparts.puchsystem;

import java.time.LocalDate;
import java.util.List;

import com.mobileparts.puchsystem.repository.EmployeeRepository;
import com.mobileparts.puchsystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mobileparts.puchsystem.model.Employee;
import com.mobileparts.puchsystem.service.EmployeeService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    public EmployeeService employeeService;
    @Autowired
    public EmailService emailService;
    @Autowired
    public EmployeeRepository employeeRepository;
    @Override
    public void run(String... args) throws Exception
    {
        System.out.println("\n");
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("    TESTING CONTRACT REPOSITORY METHODS");
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("\n");

        // ============================================
        // CREATE TEST EMPLOYEES WITH DIFFERENT CONTRACT DATES
        // ============================================
        System.out.println("📝 Creating Test Employees with Contract Dates...\n");
        Employee emp = new Employee(
                "contract",LocalDate.now().minusYears(1),
                "abinbasheer@gmail.com","Assembely","basheer","Abin","EMP001");
        emp.setContractEndDate(LocalDate.now().plusDays(30));
        employeeRepository.save(emp);
        System.out.println("✅ " + emp.getFullName() + " | Contract ends: " + emp.getContractEndDate());
        System.out.println("Hire Date : "+emp.getHireDate());

        Employee emp2 = new Employee(
                "contract",LocalDate.now().minusYears(2),
                "jiswan@gmail.com","Assembely","jiswan","Muhammed","EMP002");
        emp2.setContractEndDate(LocalDate.now().plusDays(45));
        employeeRepository.save(emp2);
        System.out.println("✅ " + emp2.getFullName() + " | Contract ends: " + emp2.getContractEndDate());
        System.out.println("Hire Date : "+emp2.getHireDate());

        Employee emp3 = new Employee(
                "contract",LocalDate.now().minusMonths(6),
                "ijas@gmail.com","Paint","Ahmed","Ijas","EMP003");
        emp3.setContractEndDate(LocalDate.now().plusDays(90));
        employeeRepository.save(emp3);
        System.out.println("✅ " + emp3.getFullName() + " | Contract ends: " + emp3.getContractEndDate());
        System.out.println("Hire Date : "+emp3.getHireDate());

        // Employee 4: Already Permanent
        Employee emp4 = new Employee(
                "Permanent", LocalDate.now().minusYears(5), "jane@gmail.com", "Production",
                "smith", "jane","EMP004");
        employeeRepository.save(emp4);
        System.out.println("✅ " + emp4.getFullName() + " | Type: Permanent (no end date)");

        System.out.println("\n─────────────────────────────────────────────────────\n");




    }

    private void createTestEmployee(String employeeType,LocalDate hireDate ,String emailId,String department,String lastName,String firstName,String employeeId)
    {
        Employee e = new Employee(employeeType,hireDate,emailId,department,lastName,firstName,employeeId);
        employeeService.createEmployee(employeeType,hireDate,emailId,department,lastName,firstName,employeeId);
    }
}
