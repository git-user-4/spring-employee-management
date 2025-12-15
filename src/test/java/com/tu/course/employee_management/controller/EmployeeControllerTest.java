package com.tu.course.employee_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tu.course.employee_management.dto.auth.RegisterRequestDTO;
import com.tu.course.employee_management.dto.employee.EmployeeResponseDTO;
import com.tu.course.employee_management.exception.ResourceNotFoundException;
import com.tu.course.employee_management.mapper.EmployeeMapper;
import com.tu.course.employee_management.model.Department;
import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.model.Role;
import com.tu.course.employee_management.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private EmployeeMapper employeeMapper;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

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
    void getAllEmployees_returnsMappedDtoList() throws Exception {
        Employee employee1 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("123456789")
                .department(new Department("Engineering"))
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Toma")
                .lastName("Tomov")
                .phoneNumber("0875721957")
                .department(new Department("Sales"))
                .build();

        List<Employee> employees = List.of(employee1, employee2);

        EmployeeResponseDTO responseDTO1 = new EmployeeResponseDTO(
                1L,
                "doe18@abv.bg",
                Role.USER,
                "John",
                "Doe",
                "123456789",
                null,
                1L,
                "Engineering"
        );

        EmployeeResponseDTO responseDTO2 = new EmployeeResponseDTO(
                2L,
                "tommy@gmail.com",
                Role.USER,
                "Toma",
                "Tomov",
                "0875721957",
                null,
                2L,
                "Sales"
        );

        List<EmployeeResponseDTO> responseDTOs = List.of(responseDTO1, responseDTO2);

        Mockito.when(employeeService.getAllEmployees())
                .thenReturn(employees);

        Mockito.when(employeeMapper.toEmployeeResponseDTOList(employees))
                .thenReturn(responseDTOs);

        mockMvc.perform(get("/employees")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(responseDTOs.get(0).id().intValue()))
                .andExpect(jsonPath("$[0].firstName").value(responseDTOs.get(0).firstName()))
                .andExpect(jsonPath("$[0].lastName").value(responseDTOs.get(0).lastName()))
                .andExpect(jsonPath("$[0].phoneNumber").value(responseDTOs.get(0).phoneNumber()))
                .andExpect(jsonPath("$[0].departmentId").value(responseDTOs.get(0).departmentId().intValue()))
                .andExpect(jsonPath("$[0].departmentName").value(responseDTOs.get(0).departmentName()))
                .andExpect(jsonPath("$[1].id").value(responseDTOs.get(1).id().intValue()))
                .andExpect(jsonPath("$[1].firstName").value(responseDTOs.get(1).firstName()))
                .andExpect(jsonPath("$[1].lastName").value(responseDTOs.get(1).lastName()))
                .andExpect(jsonPath("$[1].phoneNumber").value(responseDTOs.get(1).phoneNumber()))
                .andExpect(jsonPath("$[1].departmentId").value(responseDTOs.get(1).departmentId().intValue()))
                .andExpect(jsonPath("$[1].departmentName").value(responseDTOs.get(1).departmentName()));
    }
    // =================================================


    // ============= registerEmployee Tests =============
    @Test
    void registerEmployee_returnsMappedDto() throws Exception {
        // Given
        RegisterRequestDTO employeeRequestDTO = new RegisterRequestDTO(
                "johnny4@abv.bg",
                "_secretPass_",
                "John",
                "Doe",
                "0123456789",
                "Engineering"

        );

        Employee employee = Employee.builder()
                .email("johnny4@abv.bg")
                .password("_secretPass_")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("123456789")
                .department(new Department("Engineering"))
                .build();

        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(
                1L,
                "johnny4@abv.bg",
                Role.ADMIN,
                "John",
                "Doe",
                "123456789",
                null,
                1L,
                "Engineering"
        );

        Mockito.when(employeeService.registerEmployee(employeeRequestDTO))
                .thenReturn(employee);

        Mockito.when(employeeMapper.toEmployeeResponseDTO(employee))
                .thenReturn(responseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeRequestJson = objectMapper.writeValueAsString(employeeRequestDTO);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeRequestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(".id").value(responseDTO.id().intValue()))
                .andExpect(jsonPath(".firstName").value(responseDTO.firstName()))
                .andExpect(jsonPath(".lastName").value(responseDTO.lastName()))
                .andExpect(jsonPath(".phoneNumber").value(responseDTO.phoneNumber()))
                .andExpect(jsonPath(".departmentId").value(responseDTO.departmentId().intValue()))
                .andExpect(jsonPath(".departmentName").value(responseDTO.departmentName()));
    }
    // =================================================

}
