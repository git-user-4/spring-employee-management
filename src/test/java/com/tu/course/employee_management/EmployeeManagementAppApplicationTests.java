package com.tu.course.employee_management;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class EmployeeManagementAppApplicationTests {

    @Autowired
    EmployeeManagementAppApplication appUnderTest;

    @Test
    void contextLoads() {
        assertThat(appUnderTest).isNotNull();
    }

}
