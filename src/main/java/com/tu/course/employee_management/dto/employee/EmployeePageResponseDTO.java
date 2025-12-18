package com.tu.course.employee_management.dto.employee;

import java.util.List;

public record EmployeePageResponseDTO(
        List<EmployeeResponseDTO> employees,
        int totalPages,
        long totalElements,
        int page,
        int size
) {}
