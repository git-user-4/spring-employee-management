package com.tu.course.employee_management.dto.employee;

public record EmployeeSummaryResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber
) {}
