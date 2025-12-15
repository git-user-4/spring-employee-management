package com.tu.course.employee_management.dto.employee;

import com.tu.course.employee_management.model.Role;

public record EmployeeResponseDTO(
        Long id,
        String email,
        Role role,
        String firstName,
        String lastName,
        String phoneNumber,
        String avatarImage,
        Long departmentId,
        String departmentName
) {}
