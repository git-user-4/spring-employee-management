package com.tu.course.employee_management.controller;

import com.tu.course.employee_management.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Deprecated
@RestController
@RequiredArgsConstructor
@RequestMapping("/greet")
public class GreetingController {

    private final GreetingService service;

    @GetMapping("/{name}")
    public String greet(@PathVariable String name) {
        return service.greet(name);
    }
    
}
