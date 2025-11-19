package com.tu.course.employee_management.repository;

import org.springframework.stereotype.Repository;

@Deprecated
@Repository
public class GreetingRepository {

    public String getGreeting() {
        // Simulate DB returns value:
        // DB fetch code
        return "Hello";
    }

}
