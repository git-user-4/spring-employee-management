package com.tu.course.employee_management.startup.seeder;

import com.tu.course.employee_management.model.Department;
import com.tu.course.employee_management.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Profile("dev")
@Order(0)
@Component
@RequiredArgsConstructor
public class DepartmentSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) throws Exception {
        Department department1 = new Department("sales");
        Department department2 = new Department("construction");
        Department department3 = new Department("accounting");

        departmentRepository.save(department1);
        departmentRepository.save(department2);
        departmentRepository.save(department3);

        System.out.println("--- 3 Departments were seeded! ---");
    }

}
