package com.tu.course.employee_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tu.course.employee_management.config.security.JwtProvider;
import com.tu.course.employee_management.dto.auth.RegisterRequestDTO;
import com.tu.course.employee_management.dto.employee.EmployeePageResponseDTO;
import com.tu.course.employee_management.dto.employee.EmployeeResponseDTO;
import com.tu.course.employee_management.exception.ResourceNotFoundException;
import com.tu.course.employee_management.mapper.EmployeeMapper;
import com.tu.course.employee_management.model.Department;
import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.model.Role;
import com.tu.course.employee_management.service.CustomUserDetailsService;
import com.tu.course.employee_management.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private EmployeeMapper employeeMapper;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    // ============= getEmployeeById Tests =============
    @Test
    void getEmployeeById_returnsMappedDto() throws Exception {
        // Given
        Long id = 1L;

        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("123456789")
                .department(new Department("Engineering"))
                .build();

        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(
                id,
                "doe18@abv.bg",
                Role.USER,
                "John",
                "Doe",
                "123456789",
                null,
                1L,
                "Engineering"
        );

        Mockito.when(employeeService.getEmployeeOrThrow(id))
                .thenReturn(employee);

        Mockito.when(employeeMapper.toEmployeeResponseDTO(employee))
                .thenReturn(responseDTO);

        mockMvc.perform(get("/employees/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(responseDTO.id().intValue()))
                .andExpect(jsonPath("$.firstName").value(responseDTO.firstName()))
                .andExpect(jsonPath("$.lastName").value(responseDTO.lastName()))
                .andExpect(jsonPath("$.phoneNumber").value(responseDTO.phoneNumber()))
                .andExpect(jsonPath("$.departmentId").value(responseDTO.departmentId()))
                .andExpect(jsonPath("$.departmentName").value(responseDTO.departmentName()));
    }

    @Test
    void getEmployeeById_whenNotFound_returns404() throws Exception {
        Long id = 1L;

        // Service throws custom ErrorResponse
        Mockito.when(employeeService.getEmployeeOrThrow(id))
                .thenThrow(new ResourceNotFoundException(Employee.class, id));

        mockMvc.perform(get("/employees/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                // ProblemDetail from ResourceNotFoundException
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.id").value(id.intValue()))
                .andExpect(jsonPath("$.resource").value(Employee.class.getSimpleName()));
    }
    // =================================================


    // ============= getAllEmployees Tests =============
    @Test
    void getAllEmployees_returnsMappedDtoPage() throws Exception {
        Employee employee1 = Employee.builder()
                .firstName("John").lastName("Doe").phoneNumber("123456789")
                .department(new Department("Engineering"))
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Toma").lastName("Tomov").phoneNumber("0875721957")
                .department(new Department("Sales"))
                .build();

        List<Employee> employees = List.of(employee1, employee2);

        EmployeeResponseDTO responseDTO1 = new EmployeeResponseDTO(
                1L, "doe18@abv.bg", Role.USER,
                "John", "Doe", "123456789",
                null, 1L, "Engineering"
        );

        EmployeeResponseDTO responseDTO2 = new EmployeeResponseDTO(
                2L, "tommy@gmail.com", Role.USER,
                "Toma", "Tomov", "0875721957",
                null, 2L, "Sales"
        );

        Pageable pageable = PageRequest.of(0, 20);
        Page<Employee> employeePage = new PageImpl<>(employees, pageable, employees.size());

        EmployeePageResponseDTO wrapper = new EmployeePageResponseDTO(
                List.of(responseDTO1, responseDTO2),
                employeePage.getTotalPages(),
                employeePage.getTotalElements(),
                employeePage.getNumber(),
                employeePage.getSize()
        );

        Mockito.when(employeeService.getAllEmployees(any(Pageable.class)))
                .thenReturn(employeePage);

        Mockito.when(employeeMapper.toEmployeePageResponseDTO(employeePage))
                .thenReturn(wrapper);

        mockMvc.perform(get("/employees")
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employees[0].id").value(responseDTO1.id().intValue()))
                .andExpect(jsonPath("$.employees[0].firstName").value(responseDTO1.firstName()))
                .andExpect(jsonPath("$.employees[0].lastName").value(responseDTO1.lastName()))
                .andExpect(jsonPath("$.employees[0].phoneNumber").value(responseDTO1.phoneNumber()))
                .andExpect(jsonPath("$.employees[0].departmentId").value(responseDTO1.departmentId().intValue()))
                .andExpect(jsonPath("$.employees[0].departmentName").value(responseDTO1.departmentName()))
                .andExpect(jsonPath("$.employees[1].id").value(responseDTO2.id().intValue()))
                .andExpect(jsonPath("$.employees[1].firstName").value(responseDTO2.firstName()))
                .andExpect(jsonPath("$.employees[1].lastName").value(responseDTO2.lastName()))
                .andExpect(jsonPath("$.employees[1].phoneNumber").value(responseDTO2.phoneNumber()))
                .andExpect(jsonPath("$.employees[1].departmentId").value(responseDTO2.departmentId().intValue()))
                .andExpect(jsonPath("$.employees[1].departmentName").value(responseDTO2.departmentName()));
    }
    // =================================================

}
