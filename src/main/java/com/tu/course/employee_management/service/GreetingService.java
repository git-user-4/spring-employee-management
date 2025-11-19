package com.tu.course.employee_management.service;

import com.tu.course.employee_management.repository.GreetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Deprecated
@Service
@RequiredArgsConstructor
public class GreetingService {

    private final GreetingRepository greetingRepository;

    public String greet(String username) {
        return greetingRepository.getGreeting() + " " + username + "!";
    }

}
