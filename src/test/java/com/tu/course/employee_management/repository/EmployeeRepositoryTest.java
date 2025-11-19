package com.tu.course.employee_management.repository;

import com.tu.course.employee_management.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = {"/sql/department_data.sql", "/sql/employee_data.sql"})
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    // ============= findByPhoneNumberStartingWith Tests =============
    @Test
    void findByPhoneNumberStartingWith_returnsMatchingEmployees() {
        // Given
        String searchedStartingCombination = "088";

        // Act
        List<Employee> result = employeeRepository.findByPhoneNumberStartingWith(searchedStartingCombination);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(employee -> employee.getPhoneNumber().startsWith(searchedStartingCombination)));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    void findByPhoneNumberStartingWith_returnsEmptyList_whenNoMatch() {
        // Given
        String searchedStartingCombination = "123";

        // Act
        List<Employee> result = employeeRepository.findByPhoneNumberStartingWith(searchedStartingCombination);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    // =================================================

}
