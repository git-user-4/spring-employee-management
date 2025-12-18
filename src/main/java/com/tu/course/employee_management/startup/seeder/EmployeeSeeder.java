package com.tu.course.employee_management.startup.seeder;

import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.model.Role;
import com.tu.course.employee_management.repository.DepartmentRepository;
import com.tu.course.employee_management.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("dev")
@Order(1)
@Component
@RequiredArgsConstructor
public class EmployeeSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Employee employee1 = Employee.builder()
                .email("v.vasilev@abv.bg")
                .password(passwordEncoder.encode("myPass1234"))
                .role(Role.USER)
                .firstName("Valentin")
                .lastName("Vasilev")
                .phoneNumber("0889124123")
                .department(departmentRepository.findById(2L).orElseThrow(() -> new RuntimeException("Employee Seeding failed!")))
                .build();

        Employee employee2 = Employee.builder()
                .email("ivpetrov@gmail.com")
                .password(passwordEncoder.encode("changeMe4321"))
                .role(Role.ADMIN)
                .firstName("Ivan")
                .lastName("Petrov")
                .phoneNumber("0887512397")
                .build();

        Employee employee3 = Employee.builder()
                .email("johnny4@abv.bg")
                .password(passwordEncoder.encode("secretPass1"))
                .role(Role.USER)
                .firstName("Johnny")
                .lastName("Bravo")
                .phoneNumber("0871234123")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);

        log.info("--- 3 Employees were seeded! ---");
    }

}
