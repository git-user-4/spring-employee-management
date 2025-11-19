package com.tu.course.employee_management.mapper;

import com.tu.course.employee_management.dto.employee.EmployeeResponseDTO;
import com.tu.course.employee_management.model.Department;
import com.tu.course.employee_management.model.Employee;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeMapperTest {

    private final EmployeeMapper underTest = Mappers.getMapper(EmployeeMapper.class);

    //======================
    private static Stream<Arguments> employeeProvider() {
        Department hr = new Department("HR");
        hr.setId(1L);

        Department it = new Department("IT");
        it.setId(2L);

        return Stream.of(
                Arguments.of(
                        Employee.builder()
                                .id(1L)
                                .firstName("John")
                                .lastName("Doe")
                                .phoneNumber("08875123")
                                .department(hr)
                                .build(),
                        1L, "HR"
                ),
                Arguments.of(
                        Employee.builder()
                                .id(2L)
                                .firstName("Alice")
                                .lastName("Smith")
                                .phoneNumber("0871234123")
                                .department(null)
                                .build(),
                        null, null
                ),
                Arguments.of(
                        Employee.builder()
                                .id(3L)
                                .firstName("Bob")
                                .lastName("Brown")
                                .phoneNumber(null)
                                .department(it)
                                .build(),
                        2L, "IT"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("employeeProvider")
    void toEmployeeResponseDTOTest(Employee employee, Long expectedDeptId, String expectedDeptName) {
        EmployeeResponseDTO result = underTest.toEmployeeResponseDTO(employee);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(employee.getId());
        assertThat(result.firstName()).isEqualTo(employee.getFirstName());
        assertThat(result.lastName()).isEqualTo(employee.getLastName());
        assertThat(result.phoneNumber()).isEqualTo(employee.getPhoneNumber());

        // Department fields
        assertThat(result.departmentId()).isEqualTo(expectedDeptId);
        assertThat(result.departmentName()).isEqualTo(expectedDeptName);
    }
    //======================

    //======================
    private static Stream<Arguments> employeeListProvider() {
        Department hr = new Department("HR");
        hr.setId(1L);

        Department it = new Department("IT");
        it.setId(2L);

        List<Employee> employees = List.of(
                Employee.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .phoneNumber("08875123")
                        .department(hr)
                        .build(),
                Employee.builder()
                        .id(2L)
                        .firstName("Alice")
                        .lastName("Smith")
                        .phoneNumber("0871234123")
                        .department(null)
                        .build(),
                Employee.builder()
                        .id(3L)
                        .firstName("Bob")
                        .lastName("Brown")
                        .phoneNumber(null)
                        .department(it)
                        .build()
        );

        return Stream.of(Arguments.of(employees));
    }

    @ParameterizedTest
    @MethodSource("employeeListProvider")
    void toEmployeeResponseDTOListTest(List<Employee> employees) {
        // When
        List<EmployeeResponseDTO> result = underTest.toEmployeeResponseDTOList(employees);

        // Then
        assertThat(result).hasSize(employees.size());

        for (int i = 0; i < employees.size(); i++) {
            Employee source = employees.get(i);
            EmployeeResponseDTO dto = result.get(i);

            // Basic fields
            assertThat(dto.id()).isEqualTo(source.getId());
            assertThat(dto.firstName()).isEqualTo(source.getFirstName());
            assertThat(dto.lastName()).isEqualTo(source.getLastName());
            assertThat(dto.phoneNumber()).isEqualTo(source.getPhoneNumber());

            // Department fields
            if (source.getDepartment() == null) {
                assertThat(dto.departmentId()).isNull();
                assertThat(dto.departmentName()).isNull();
            } else {
                assertThat(dto.departmentId()).isEqualTo(source.getDepartment().getId());
                assertThat(dto.departmentName()).isEqualTo(source.getDepartment().getName());
            }
        }
    }
    //======================

}
