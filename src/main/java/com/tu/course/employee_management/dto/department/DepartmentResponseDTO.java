package com.tu.course.employee_management.dto.department;

import com.tu.course.employee_management.dto.employee.EmployeeSummaryResponseDTO;

import java.util.List;

public record DepartmentResponseDTO(
        Long id,
        String name,
        List<EmployeeSummaryResponseDTO> employees
) {}
