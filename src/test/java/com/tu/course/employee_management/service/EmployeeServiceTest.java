package com.tu.course.employee_management.service;

import com.tu.course.employee_management.dto.employee.EmployeeRequestDTO;
import com.tu.course.employee_management.exception.ResourceNotFoundException;
import com.tu.course.employee_management.mapper.EmployeeMapper;
import com.tu.course.employee_management.model.Department;
import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;
    private EmployeeMapper employeeMapper;
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        departmentService = mock(DepartmentService.class);
        employeeMapper = mock(EmployeeMapper.class);
        employeeService = new EmployeeService(employeeRepository, departmentService, employeeMapper);
    }

    // ============= getEmployeeOrThrow Tests =============
    @Test
    void getEmployeeOrThrow_returnsEmployee_whenFound() {
        // Arrange
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("0123456789")
                .department(new Department("Engineering"))
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act
        Employee result = employeeService.getEmployeeOrThrow(1L);

        // Assert
        assertNotNull(result);
        assertEquals(employee.getId(), result.getId());
        assertEquals(employee.getFirstName(), result.getFirstName());
        assertEquals(employee.getLastName(), result.getLastName());
        assertEquals(employee.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(employee.getDepartment(), result.getDepartment());

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployeeOrThrow_throwsException_whenNotFound() {
        // Arrange
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getEmployeeOrThrow(99L));

        verify(employeeRepository, times(1)).findById(99L);
    }
    // =================================================


    // ============= saveEmployee Tests =============
    @Test
    void saveEmployee_savesEmployeeWithDepartment_whenDepartmentNameProvided() {
        // Arrange
        EmployeeRequestDTO request = new EmployeeRequestDTO(
                "John",
                "Doe",
                "0123456789",
                "Engineering"
        );

        Department department = new Department(request.departmentName());

        Employee employeeMapped = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("0123456789")
                .department(department)
                .build();

        Employee savedEmployee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("0123456789")
                .department(department)
                .build();

        // Stub Mapper
        when(employeeMapper.toEmployee(request)).thenReturn(employeeMapped);

        // Stub Department Fetch/Creation
        when(departmentService.getDepartmentByNameOrCreate(request.departmentName()))
                .thenReturn(department);

        // Stub Repo Save
        when(employeeRepository.save(employeeMapped)).thenReturn(savedEmployee);

        // Act
        Employee result = employeeService.saveEmployee(request);

        // Assert
        assertNotNull(result);
        assertEquals(savedEmployee.getId(), result.getId());
        assertEquals(department, result.getDepartment());

        // Verify Correct Interactions
        verify(departmentService, times(1))
                .getDepartmentByNameOrCreate(request.departmentName());

        verify(employeeRepository, times(1)).save(employeeMapped);
    }

    @Test
    void saveEmployee_savesEmployeeWithoutDepartment_whenDepartmentNameIsNull() {
        // Arrange
        EmployeeRequestDTO request = new EmployeeRequestDTO(
                "John",
                "Doe",
                "0123456789",
                null
        );

        Employee employeeMapped = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("0123456789")
                .build();

        Employee savedEmployee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("0123456789")
                .build();

        when(employeeMapper.toEmployee(request)).thenReturn(employeeMapped);
        when(employeeRepository.save(employeeMapped)).thenReturn(savedEmployee);

        // Act
        Employee result = employeeService.saveEmployee(request);

        // Assert
        assertNotNull(result);
        assertEquals(savedEmployee.getId(), result.getId());
        assertNull(result.getDepartment());

        // DepartmentService MUST NOT be called
        verify(departmentService, never()).getDepartmentByNameOrCreate(anyString());

        verify(employeeRepository, times(1)).save(employeeMapped);
    }
    // =================================================

}
