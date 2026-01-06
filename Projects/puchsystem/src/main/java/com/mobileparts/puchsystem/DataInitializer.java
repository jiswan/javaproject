package com.mobileparts.puchsystem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import com.mobileparts.puchsystem.repository.EmployeeRepository;
import com.mobileparts.puchsystem.service.ContractExtensionService;
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
    @Autowired
    public ContractExtensionService contractExtensionService;
    @Override
    public void run(String... args) throws Exception
    {
        System.out.println("\n");
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("    CONTRACT MANAGEMENT WORKFLOW TEST      ");
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("\n");

        // ============================================
        // CREATE TEST EMPLOYEES WITH DIFFERENT CONTRACT DATES
        // ============================================
        System.out.println("SCENARIO SETUP: Creating Test Employees\n");
        System.out.println("─────────────────────────────────────────────────────\n");
        Employee emp1 = new Employee(
                "Contract",LocalDate.now().minusYears(1),
                "emp1@mmc.com","Assembely","Smith","John","EMP001");
        emp1.setContractEndDate(LocalDate.now().plusDays(20));
        employeeRepository.save(emp1);
        System.out.println("John Smith | Contract | 1 year service | Expires in 20 days");

        Employee emp2 = new Employee(
                "Contract",LocalDate.now().minusYears(3),
                "emp2@mmc.com","Assembly","Johnson","Sarah","EMP002");
        emp2.setContractEndDate(LocalDate.now().plusDays(35));
        employeeRepository.save(emp2);
        System.out.println("Sarah Johnson | Contract | 3 years service | Expires in 35 days");

        Employee emp3 = new Employee(
                "Contract",LocalDate.now().minusYears(2),
                "emp3@mmc.com","Paint","Williams","Mikw","EMP003");
        emp3.setContractEndDate(LocalDate.now().plusDays(50));
        employeeRepository.save(emp3);
        System.out.println("Mike Williams | Contract | 2 years service | Expires in 50 days");

        Employee emp4 = new Employee(
                "Contract",LocalDate.now().minusMonths(6),
                "emp4@mmc.com","Production","Emily","Brown","EMP004");
        emp4.setContractEndDate(LocalDate.now().plusDays(90));
        employeeRepository.save(emp4);
        System.out.println("Emily Brown | Contract | 6 months service | Expires in 90 days (too far)");

        Employee emp5 = new Employee(
                "Permanent",LocalDate.now().minusYears(5),
                "emp5@mmc.com","Engineering","Davis","RObert","EMP005");
        employeeRepository.save(emp5);
        System.out.println("Robert Davis | PERMANENT | 5 years service");

        Employee emp6 = new Employee(
                "Contract",LocalDate.now().minusYears(1).minusDays(6),
                "emp6@mmc.com","Quality","Wilson","Lisa","EMP006");
        emp6.setContractEndDate(LocalDate.now().minusDays(10));
        employeeRepository.save(emp6);
        System.out.println(" Lisa Wilson | Contract | Expired 10 days ago");

        Employee emp7 = new Employee(
                "Contract",LocalDate.now().minusMonths(6),
                "emp7@mmc.com","Logistic","Martinez","Carlos","EMP007");
        emp7.setContractEndDate(LocalDate.now().plusDays(55));
        employeeRepository.save(emp7);;
        System.out.println("Carlos Martinez | Contract | 6 months service | Expires in 55 days");
        System.out.println("\n─────────────────────────────────────────────────────\n");

        // ============================================
        // STEP 1: Find Expiring Contracts
        // ============================================
        System.out.println("🔍 STEP 1: System Checks for Expiring Contracts\n");
        LocalDate sixtyDayFromNow = LocalDate.now().plusDays(60);
        System.out.println("Today: "+LocalDate.now());
        System.out.println("60 Days cutoff: "+sixtyDayFromNow);
        System.out.println();

        List<Employee> expiring = employeeRepository.findByEmployeeTypeAndContractEndDateBefore("Contract",sixtyDayFromNow);
        System.out.println("Found: "+expiring.size()+ " contracts expiring in next 60 days:");
        for(Employee e : expiring)
        {
            long yearsOfService = ChronoUnit.YEARS.between(e.getHireDate(),LocalDate.now());
            long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(),e.getContractEndDate());
            System.out.println("  -"+e.getFullName()+
                    "| Expire in "+daysUntil+"days"+
                    "| "+yearsOfService+"years of service");
        }
        System.out.println("\n─────────────────────────────────────────────────────\n");
        // ============================================
        // STEP 2: Send Extension Emails
        // ============================================
        contractExtensionService.sendContractExtensionEmails();

        // ============================================
        // STEP 3: Employees Respond
        // ============================================
        System.out.println("👥 STEP 3: Employees Respond to Offers\n");
        System.out.println("Scenario A: John Smith accepts 1-year extension");
        Optional<Employee> johnOpt = employeeRepository.findByEmailId("emp1@mmc.com");
        if(johnOpt.isPresent())
        {
            String johnToken = johnOpt.get().getExtensionToken();
            String result = contractExtensionService.acceptContractExtension(johnToken);
            System.out.println(result);
        }
        System.out.println();

        //accepts permanent position
        System.out.println("Scenario B: Sarah Johnson accepts PERMANENT position");
        Optional<Employee> sarahOpt = employeeRepository.findByEmailId("emp2@mmc.com");
        if (sarahOpt.isPresent())
        {
            String sarahToken = sarahOpt.get().getExtensionToken();
            String result = contractExtensionService.acceptPermanentPosition(sarahToken);
            System.out.println(result);
        }
        System.out.println();

        // Mike chooses extension instead of permanent
        System.out.println(" Scenario C: Mike Williams chooses EXTENSION (even though eligible for permanent)");
        Optional<Employee> mikeOpt = employeeRepository.findByEmailId("emp3@mmc.com");
        if(mikeOpt.isPresent())
        {
            String mikeToken = mikeOpt.get().getExtensionToken();
            String result = contractExtensionService.acceptContractExtension(mikeToken);
            System.out.println(result);
        }
        System.out.println();

        System.out.println();

        // Carlos ignores email (no action)
        System.out.println("📌 Scenario D: Carlos Martinez IGNORES email (no action taken)");
        System.out.println("   ⏳ Carlos has not responded yet");
        System.out.println();

        System.out.println("\n─────────────────────────────────────────────────────\n");

        // ============================================
        // STEP 4: Verify Final State
        // ============================================
        System.out.println("📊 STEP 4: Final System State Report\n");

        List<Employee> allEmployees = employeeRepository.findAll();

        System.out.println("╔════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    EMPLOYEE STATUS REPORT                              ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════╝\n");

        for (Employee emp : allEmployees) {
            System.out.println("Employee: " + emp.getFullName());
            System.out.println("   Type: " + emp.getEmployeeType());
            System.out.println("   Contract End: " +
                    (emp.getContractEndDate() != null ? emp.getContractEndDate() : "N/A (Permanent)"));
            System.out.println("   Extension Pending: " + emp.isExtensionRequested());
            System.out.println("   Years of Service: " +
                    ChronoUnit.YEARS.between(emp.getHireDate(), LocalDate.now()));
            System.out.println();
        }

        System.out.println("─────────────────────────────────────────────────────\n");

        // ============================================
        // STEP 5: Statistics Summary
        // ============================================
        System.out.println("📈 STEP 5: Statistics Summary\n");

        long totalEmployees = employeeRepository.count();
        long permanentCount = employeeRepository.countByEmployeeType("Permanent");
        long contractCount = employeeRepository.countByEmployeeType("Contract");
        long pendingRequests = employeeRepository.countByExtensionRequested(true);

        System.out.println("Total Employees: " + totalEmployees);
        System.out.println("Permanent Employees: " + permanentCount);
        System.out.println("Contract Employees: " + contractCount);
        System.out.println("Pending Extension Requests: " + pendingRequests);
        System.out.println();

        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║          WORKFLOW TEST COMPLETE! ✅                    ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

    }



}
