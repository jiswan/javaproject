package com.mobileparts.puchsystem.service;

import com.mobileparts.puchsystem.model.Employee;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    /**
     * Send email - currently just prints to console
     */
    public void sendEmail(String to, String subject, String body) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📧 EMAIL SENT");
        System.out.println("=".repeat(70));
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("-".repeat(70));
        System.out.println(body);
        System.out.println("=".repeat(70) + "\n");
    }

    /**
     * Send confirmation email to employee
     */
    public void sendConfirmationEmail(Employee emp, String subject, String message) {
        String body = String.format("""
                        Dear %s,

                        %s

                        If you have any questions, please contact HR.

                        Best regards,
                        HR Department
                        """,
                emp.getFullName(),
                message
        );
        sendEmail(emp.getEmailId(), subject, body);
    }

}
