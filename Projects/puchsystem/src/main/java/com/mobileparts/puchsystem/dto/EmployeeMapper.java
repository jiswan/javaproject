package com.mobileparts.puchsystem.dto;

import com.mobileparts.puchsystem.model.Employee;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {

    //convert the employeeEntity to EmployeeDto
    public static  EmployeeDTO toDTO(Employee employee)
    {
        if(employee ==null)
        {
            return null;
        }
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFullName(employee.getFullName());
        dto.setDepartment(employee.getDepartment());
        dto.setEmailId(employee.getEmailId());
        dto.setActive(employee.isActive());
        dto.setEmployeeType(employee.getEmployeeType());
        dto.setContractEndDate(employee.getContractEndDate());

        return dto;
    }

    // Convert List of Employee entities to List of EmployeeDTOs
    public static List<EmployeeDTO> toDTOList(List<Employee> employees)
    {
        if(employees == null)
        {
            return  null;
        }

        return employees.stream()
                .map(EmployeeMapper::toDTO).collect(Collectors.toList());

    }

     // Convert EmployeeDTO to Employee entity

    public static Employee toEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }

        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setEmailId(dto.getEmailId());
        employee.setDepartment(dto.getDepartment());
        employee.setEmployeeType(dto.getEmployeeType());
        employee.setActive(dto.isActive());
        employee.setContractEndDate(dto.getContractEndDate());

        return employee;
    }




}
