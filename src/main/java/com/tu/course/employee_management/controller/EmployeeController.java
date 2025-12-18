package com.tu.course.employee_management.controller;

import com.tu.course.employee_management.dto.employee.EmployeeNameProjectionDTO;
import com.tu.course.employee_management.dto.auth.RegisterRequestDTO;
import com.tu.course.employee_management.dto.employee.EmployeePageResponseDTO;
import com.tu.course.employee_management.dto.employee.EmployeeResponseDTO;
import com.tu.course.employee_management.dto.employee.EmployeeNameResponseDTO;
import com.tu.course.employee_management.mapper.EmployeeMapper;
import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.model.Role;
import com.tu.course.employee_management.repository.projection.EmployeeNameProjection;
import com.tu.course.employee_management.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/employees")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Employee Controller", description = "Operations about employee users")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @PreAuthorize("hasRole('ADMIN') or #id == principal.employeeId")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable @Positive Long id) {
        Employee fetchedEmployee = employeeService.getEmployeeOrThrow(id);
        return new ResponseEntity<>(employeeMapper.toEmployeeResponseDTO(fetchedEmployee), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<EmployeePageResponseDTO> getAllEmployees(
            @ParameterObject @PageableDefault(size = 2, sort = "id") Pageable pageable
    ) {
        Page<Employee> page = employeeService.getAllEmployees(pageable);
        return new ResponseEntity<>(employeeMapper.toEmployeePageResponseDTO(page), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable @Positive Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.employeeId")
    @PatchMapping("/{id}/firstName")
    public ResponseEntity<EmployeeResponseDTO> patchEmployeeFirstName(@PathVariable @Positive Long id,
                                                                      @RequestParam @NotBlank @Size(min = 2, max = 20) String newFirstName) {
        Employee patchedEmployee = employeeService.patchEmployeeFirstName(id, newFirstName);
        return new ResponseEntity<>(employeeMapper.toEmployeeResponseDTO(patchedEmployee), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.employeeId")
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
        return new ResponseEntity<>(HttpStatus.OK);
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

    @PreAuthorize("hasRole('ADMIN') or #id == principal.employeeId")
    @PatchMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeResponseDTO> patchEmployeeAvatar(@PathVariable @Positive Long id,
                                                                   @RequestParam @NotBlank MultipartFile file) {
        Employee patchedEmployee = employeeService.patchEmployeeAvatar(id, file);
        return new ResponseEntity<>(employeeMapper.toEmployeeResponseDTO(patchedEmployee), HttpStatus.OK);
    }

    @Operation(summary = "Delete employee's profile picture (avatar) (Requires: ADMIN/SAME USER)")
    @ApiResponse(responseCode = "200", description = "Avatar deleted")
    @ApiResponse(responseCode = "403", description = "User Unauthorized")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.employeeId")
    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<Void> deleteEmployeeAvatar(@PathVariable @Positive Long id) {
        employeeService.deleteEmployeeAvatar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Change employee's role (Requires: ADMIN)",
            description = "Returns the employee with updated role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Role Updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "403",
                    description = "User Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/{id}/role")
    public ResponseEntity<EmployeeResponseDTO> patchEmployeeRole(
            @Parameter(description = "Employee id", example = "2", required = true)
            @PathVariable @Positive Long id,

            @Parameter(description = "Role to be assigned", example = "ADMIN", required = true)
            @RequestParam @NotBlank Role newRole) {
        Employee updatedEmployee = employeeService.patchEmployeeRole(id, newRole);
        return new ResponseEntity<>(employeeMapper.toEmployeeResponseDTO(updatedEmployee), HttpStatus.OK);
    }

}
