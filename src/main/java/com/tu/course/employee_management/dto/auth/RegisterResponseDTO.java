package com.tu.course.employee_management.dto.auth;

import com.tu.course.employee_management.dto.employee.EmployeeResponseDTO;

public record RegisterResponseDTO(
        String jwtToken,
        EmployeeResponseDTO employeeData
) {}
