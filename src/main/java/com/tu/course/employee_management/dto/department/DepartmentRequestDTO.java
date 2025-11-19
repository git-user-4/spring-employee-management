package com.tu.course.employee_management.dto.department;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepartmentRequestDTO(
        @NotNull(message = "Department name cannot be null or empty")
        @Size(min = 3, max = 15, message = "Department name should be between 3 and 15 characters")
        String name
) {
    public DepartmentRequestDTO {
        if (name != null) name = name.trim();
    }
}
