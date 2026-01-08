package com.mobileparts.puchsystem.dto;

import java.time.LocalDate;

public class EmployeeDTO {

    private   String employeeId;
    private   String fullName;
    private   String department;
    private   String emailId;
    private   boolean isActive;
    private   String  employeeType;
    private LocalDate contractEndDate;

    public EmployeeDTO() {
    }

    public EmployeeDTO(String employeeId, String fullName, String department, String emailId, boolean isActive, String employeeType, LocalDate contractEndDate) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.department = department;
        this.emailId = emailId;
        this.isActive = isActive;
        this.employeeType = employeeType;
        this.contractEndDate = contractEndDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public LocalDate getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(LocalDate contractEndDate) {
        this.contractEndDate = contractEndDate;
    }
}
