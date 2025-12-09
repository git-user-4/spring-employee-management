package com.tu.course.employee_management.controller;

import com.tu.course.employee_management.dto.employee.EmployeeNameProjectionDTO;
import com.tu.course.employee_management.dto.auth.RegisterRequestDTO;
import com.tu.course.employee_management.dto.employee.EmployeeResponseDTO;
import com.tu.course.employee_management.dto.employee.EmployeeNameResponseDTO;
import com.tu.course.employee_management.mapper.EmployeeMapper;
import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.repository.projection.EmployeeNameProjection;
import com.tu.course.employee_management.service.EmployeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable @Positive Long id) {
        Employee fetchedEmployee = employeeService.getEmployeeOrThrow(id);
        return ResponseEntity.ok(employeeMapper.toEmployeeResponseDTO(fetchedEmployee));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        return new ResponseEntity<>(employeeMapper.toEmployeeResponseDTOList(employeeService.getAllEmployees()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable @Positive Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/firstName")
    public ResponseEntity<EmployeeResponseDTO> patchEmployeeFirstName(@PathVariable @Positive Long id,
                                                                      @RequestParam @NotBlank @Size(min = 2, max = 20) String newFirstName) {
        Employee patchedEmployee = employeeService.patchEmployeeFirstName(id, newFirstName);
        return ResponseEntity.ok(employeeMapper.toEmployeeResponseDTO(patchedEmployee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> putEmployee(@PathVariable @Positive Long id,
                                                           @Valid @RequestBody RegisterRequestDTO employeeRequestDTO) {
        Employee newEmployee = employeeService.putEmployee(id, employeeRequestDTO);
        return new ResponseEntity<>(employeeMapper.toEmployeeResponseDTO(newEmployee), HttpStatus.OK);
    }

    @PutMapping("/{employeeId}/department/{departmentId}")
    public ResponseEntity<EmployeeResponseDTO> assignEmployeeDepartment(@PathVariable @Positive Long employeeId,
                                                                        @PathVariable @Positive Long departmentId) {
        Employee employee = employeeService.assignEmployeeDepartment(employeeId, departmentId);
        return new ResponseEntity<>(employeeMapper.toEmployeeResponseDTO(employee), HttpStatus.OK);
    }

    @DeleteMapping("/{employeeId}/departmentViaQuery")
    public ResponseEntity<Void> deleteEmployeeDepartmentViaQuery(@PathVariable @Positive Long employeeId) {
        employeeService.deleteEmployeeDepartmentViaQuery(employeeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter/phoneNumberStartSequence")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesWithPhoneNumberStartingWith(@RequestParam @NotBlank String phoneStartCombination) {
        List<Employee> filteredEmployees = employeeService.getEmployeesWithPhoneNumberStartingWith(phoneStartCombination);
        return new ResponseEntity<>(employeeMapper.toEmployeeResponseDTOList(filteredEmployees), HttpStatus.OK);
    }

    @GetMapping("/filter/lastNameSameDepartment")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesSameLastNameSameDepartment(@RequestParam @NotBlank String lastName) {
        List<Employee> filteredEmployees = employeeService.getEmployeesWithSameLastNameInSameDepartment(lastName);
        return new ResponseEntity<>(employeeMapper.toEmployeeResponseDTOList(filteredEmployees), HttpStatus.OK);
    }

    @GetMapping("/filter/lastName")
    public ResponseEntity<List<EmployeeNameResponseDTO>> getEmployeesByLastName(@RequestParam @NotBlank String lastName) {
        List<EmployeeNameProjection> employeeNameProjections = employeeService.getEmployeeNamesByLastName(lastName);
        return new ResponseEntity<>(employeeMapper.toEmployeeNameResponseDTOList(employeeNameProjections), HttpStatus.OK);
    }

    @GetMapping("/filter/firstName")
    public ResponseEntity<List<EmployeeNameResponseDTO>> getEmployeesByFirstName(@RequestParam @NotBlank String firstName) {
        List<EmployeeNameProjectionDTO> employeeNameProjectionDTOs = employeeService.getEmployeeNamesByFirstName(firstName);
        return new ResponseEntity<>(employeeMapper.toEmployeeNameResponseDTOListFromProjectionDTOList(employeeNameProjectionDTOs), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeResponseDTO> patchEmployeeAvatar(@PathVariable @Positive Long id,
                                                                   @RequestParam MultipartFile file) {
        Employee patchedEmployee = employeeService.patchEmployeeAvatar(id, file);
        return ResponseEntity.ok(employeeMapper.toEmployeeResponseDTO(patchedEmployee));
    }

    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<Void> deleteEmployeeAvatar(@PathVariable @Positive Long id) {
        employeeService.deleteEmployeeAvatar(id);
        return ResponseEntity.ok().build();
    }

}
