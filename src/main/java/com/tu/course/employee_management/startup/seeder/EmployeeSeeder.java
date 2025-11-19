package com.tu.course.employee_management.startup.seeder;

import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.repository.DepartmentRepository;
import com.tu.course.employee_management.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Profile("dev")
@Order(1)
@Component
@RequiredArgsConstructor
public class EmployeeSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) throws Exception {
        Employee employee1 = Employee.builder()
                .firstName("Valentin")
                .lastName("Vasilev")
                .phoneNumber("0889124123")
                .department(departmentRepository.findById(2L).orElseThrow(() -> new RuntimeException("Employee Seeding failed!")))
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Ivan")
                .lastName("Petrov")
                .phoneNumber("08875123")
                .build();

        Employee employee3 = Employee.builder()
                .firstName("Johnny")
                .lastName("Bravo")
                .phoneNumber("0871234123")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);

        System.out.println("--- 3 Employees were seeded! ---");
    }

}
