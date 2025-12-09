package com.tu.course.employee_management.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
        String password,

        @NotBlank(message = "Employee first name cannot be null or empty")
        @Size(min = 2, max = 20, message = "Employee first name must be between 2 and 20 characters")
        String firstName,

        @NotBlank(message = "Employee last name cannot be null or empty")
        @Size(min = 2, max = 20, message = "Employee last name must be between 2 and 20 characters")
        String lastName,

//        @Pattern(regexp = ".*\\S.*", message = "If phone number is provided, it must not be blank")
//        @Size(min = 10, max = 10, message = "Phone Number should be exactly 10 digits")
        @Pattern(regexp = "\\d{10}", message = "If phone number is provided, it must be exactly 10 digits")
        String phoneNumber,

        @Size(min = 2, max = 25, message = "Department name must be between 2 and 25 characters")
        String departmentName
) {
    public RegisterRequestDTO {
        email = safeTrim(email);
        //don't trim password
        firstName = safeTrim(firstName);
        lastName = safeTrim(lastName);
        phoneNumber = safeTrim(phoneNumber);
        departmentName = safeTrim(departmentName);
    }

    private String safeTrim(String str) {
        return str != null ? str.trim() : null;
    }

}
