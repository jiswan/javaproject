package com.mobileparts.puchsystem.exception;

public class EmployeeNotFoundException extends  RuntimeException{

    public  EmployeeNotFoundException(String message)
    {
        super(message);
    }
    public  EmployeeNotFoundException(String employeeId, String field)
    {
        super("Employee not found with "+field+":"+employeeId);
    }
}
