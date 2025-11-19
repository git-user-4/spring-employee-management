package com.tu.course.employee_management.startup;

import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("benchmark")
@Component
@RequiredArgsConstructor
public class InsertBenchmarkRunner implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        int totalRecords = 10000;

        long start = System.currentTimeMillis();

        for (int i = 0; i < totalRecords; i++) {
            Employee emp = Employee.builder()
                    .firstName("First" + i)
                    .lastName("Last" + i)
                    .phoneNumber("Phone" + i)
                    .build();

            employeeRepository.save(emp);
        }

        long end = System.currentTimeMillis();
        System.out.println("--- Inserted " + totalRecords + " records in " + (end - start) + " ms ---");
    }

}
