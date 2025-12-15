package com.mobileparts.puchsystem.service;

import com.mobileparts.puchsystem.model.Employee;
import com.mobileparts.puchsystem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ContractExtensionService {
    @Autowired
    public EmployeeRepository employeeRepository;
    @Autowired
    public EmailService emailService;

    private List<Employee> findContractsExpiringSoon()
    {
        LocalDate sixtyDaysFromNow = LocalDate.now().plusDays(60);
        List<Employee> expiringContracts = employeeRepository.findByEmployeeTypeAndContractEndDateBefore(
                "Contract",sixtyDaysFromNow);
        // Filter out employees who already received extension request
        List<Employee> needingExtension = new ArrayList<>();
        for(Employee e : expiringContracts)
        {
            // Only include if extension hasn't been requested yet
            if(!e.isExtensionRequested())
            {
                // Also check that contract hasn't already expired
                if(e.getContractEndDate().isAfter(LocalDate.now()))
                {
                    needingExtension.add(e);
                }
            }
        }
        return  needingExtension;
    }

    //Token Generation
    private String genrateExtensionToken()
    {
        return UUID.randomUUID().toString();
    }

    private Boolean qualifiesForPermanent(Employee employee)
    {
        long yearsOfService = ChronoUnit.YEARS.between(
                employee.getHireDate(),
                LocalDate.now());
         return yearsOfService>=3;
    }
    /**
     * Build personalized email content for extension offer
     */
    private String buildExtensionEmail(Employee employee, String token,
                                       long daysUntilExpiry, boolean isPermanentOffer) {
        StringBuilder email = new StringBuilder();

        email.append("Dear ").append(employee.getFirstName()).append(",\n\n");

        email.append("Your contract with *** is approaching its end date of ")
                .append(employee.getContractEndDate())
                .append(" (").append(daysUntilExpiry).append(" days from now).\n\n");

        if (isPermanentOffer) {
            // Offer permanent position
            email.append("We are pleased to offer you a PERMANENT POSITION with ***!\n\n");
            email.append("Your hard work and dedication over the past ")
                    .append(ChronoUnit.YEARS.between(employee.getHireDate(), LocalDate.now()))
                    .append(" years have been exceptional.\n\n");
            email.append("To accept the permanent position offer, click here:\n");
            email.append("http://companyname.com/api/contracts/permanent?token=").append(token);
            email.append("\n\n");
            email.append("Or, if you prefer a 1-year contract extension instead, click here:\n");
            email.append("http://companyname.com/api/contracts/extend?token=").append(token);
        } else {
            // Offer 1-year extension
            email.append("We would like to extend your contract for another year!\n\n");
            email.append("To accept this 1-year extension, click here:\n");
            email.append("http://companyname.com/api/contracts/extend?token=").append(token);
        }

        email.append("\n\n");
        email.append("This offer will expire in 14 days.\n");
        email.append("If you have any questions, please contact HR.\n\n");
        email.append("Best regards,\n");
        email.append("*** Human Resources\n");

        return email.toString();
    }
    /**
     * Send contract extension emails to all eligible employees
     * This is the automated workflow that runs periodically
     */
    public void sendContractExtensionEmails()
    {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║   AUTOMATED CONTRACT EXTENSION EMAIL PROCESS  ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        // Step 1: Find employees who need extension emails

        List<Employee> employeesNeedingExtension = findContractsExpiringSoon();
        if (employeesNeedingExtension.isEmpty())
        {
            System.out.println(" No contracts expiring in next 60 days.");
            System.out.println("   All employees are current!\n");
            return;
        }
        System.out.println("Found " + employeesNeedingExtension.size()
                + " employee(s) with expiring contracts:\n");
        int emailSend =0;
        for(Employee e : employeesNeedingExtension)
        {
            try {

                String token = genrateExtensionToken();
                e.setExtensionToken(token);
                e.setExtensionRequested(true);
                employeeRepository.save(e);

                long dayUntilExpiry = ChronoUnit.DAYS.between(
                        LocalDate.now(),e.getContractEndDate());

                boolean isPermanentOffer =qualifiesForPermanent(e);
                String emailSubject = "Contract Extension Offer - Action Required";
                String emailBody = buildExtensionEmail(e, token,
                        dayUntilExpiry, isPermanentOffer);

                emailService.sendEmail(
                        e.getEmailId(),
                        emailSubject,
                        emailBody);
                emailSend++;
                // Log what was sent
                System.out.println("✉️  Sent to: " + e.getFullName());
                System.out.println("   Email: " + e.getEmailId());
                System.out.println("   Contract ends in: " + dayUntilExpiry + " days");
                System.out.println("   Offer type: " +
                        (isPermanentOffer ? "PERMANENT POSITION" : "1-YEAR EXTENSION"));
                System.out.println("   Token: " + token.substring(0, 8) + "...");
                System.out.println();
                }
            catch (Exception w)
            {
                System.err.println("Failed to send email to "
                        + e.getFullName() + ": " + w.getMessage());
            }


        }
        System.out.println("─────────────────────────────────────────────────");
        System.out.println("📊 Summary: Sent " + emailSend + " extension emails");
        System.out.println("═════════════════════════════════════════════════\n");

    }
}
