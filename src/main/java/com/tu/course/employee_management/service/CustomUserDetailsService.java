package com.tu.course.employee_management.service;

import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

        // Create user with no roles/authorities
        return User.builder()
                .username(employee.getEmail()) // Spring internally calls this "username"
                .password(employee.getPassword())
                .authorities(new String[]{})
                .build();
    }

}
