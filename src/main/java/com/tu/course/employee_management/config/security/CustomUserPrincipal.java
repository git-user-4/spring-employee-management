package com.tu.course.employee_management.config.security;

import com.tu.course.employee_management.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserPrincipal implements UserDetails {
    private final Employee employee;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + employee.getRole().name()));
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmail();
    }

    public Long getDepartmentId() {
        return employee.getDepartment() != null ? employee.getDepartment().getId() : null;
    }

    public Long getEmployeeId() {
        return employee.getId();
    }

    // Does the same as the required overridden getUsername()
    public String getEmail() {
        return employee.getEmail();
    }

}
