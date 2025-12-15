package com.tu.course.employee_management.controller;

import com.tu.course.employee_management.config.security.CustomUserPrincipal;
import com.tu.course.employee_management.config.security.JwtProvider;
import com.tu.course.employee_management.dto.auth.LoginRequestDTO;
import com.tu.course.employee_management.dto.auth.LoginResponseDTO;
import com.tu.course.employee_management.dto.auth.RegisterRequestDTO;
import com.tu.course.employee_management.dto.auth.RegisterResponseDTO;
import com.tu.course.employee_management.mapper.EmployeeMapper;
import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.email(),
                        loginRequestDTO.password()
                )
        );

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

        String token = jwtProvider.generateToken(
                principal.getEmail(),
                principal.getAuthorities().iterator().next().getAuthority()
        );

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO) {

        Employee registeredEmployee = employeeService.registerEmployee(registerRequestDTO);

        String token = jwtProvider.generateToken(
                registeredEmployee.getEmail(),
                "ROLE_" + registeredEmployee.getRole().name()
        );

        RegisterResponseDTO registerResponseDTO =
                employeeMapper.toRegisterResponseDTO(registeredEmployee, token);

        return new ResponseEntity<>(registerResponseDTO, HttpStatus.CREATED);
    }

}
