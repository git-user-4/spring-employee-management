package com.tu.course.employee_management.startup;

import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
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
        log.info("--- Inserted {} records in {}ms ---", totalRecords, (end - start));
    }

}
