package com.tu.course.employee_management.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @Schema(description = "User's email address", example = "my_mail@gmail.com", format = "email")
        @NotBlank
        @Email
        String email,

        @Schema(description = "User's password", example = "myPass1234")
        @NotBlank
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
        String password
) {}
