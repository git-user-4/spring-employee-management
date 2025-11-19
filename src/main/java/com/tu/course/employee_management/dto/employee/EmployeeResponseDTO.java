package com.tu.course.employee_management.dto.employee;

public record EmployeeResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        Long departmentId,
        String departmentName
) {}
