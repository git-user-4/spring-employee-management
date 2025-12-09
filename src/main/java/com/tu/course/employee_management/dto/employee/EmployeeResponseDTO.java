package com.tu.course.employee_management.dto.employee;

public record EmployeeResponseDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String avatarImage,
        Long departmentId,
        String departmentName
) {}
