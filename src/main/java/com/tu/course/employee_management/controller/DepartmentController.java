package com.tu.course.employee_management.controller;

import com.tu.course.employee_management.dto.department.DepartmentRequestDTO;
import com.tu.course.employee_management.dto.department.DepartmentResponseDTO;
import com.tu.course.employee_management.mapper.DepartmentMapper;
import com.tu.course.employee_management.model.Department;
import com.tu.course.employee_management.service.DepartmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/departments")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@Validated
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @PostMapping()
    public ResponseEntity<DepartmentResponseDTO> saveDepartment(@Valid @RequestBody DepartmentRequestDTO departmentRequestDTO) {
        Department savedDepartment = departmentService.saveDepartment(departmentRequestDTO);
        return new ResponseEntity<>(departmentMapper.toDepartmentResponseDTO(savedDepartment), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable @Positive Long id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.departmentId")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable @Positive Long id) {
        Department fetchedDepartment = departmentService.getDepartmentOrThrow(id);
        return new ResponseEntity<>(departmentMapper.toDepartmentResponseDTO(fetchedDepartment), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        return new ResponseEntity<>(departmentMapper.toDepartmentResponseDTOList(departmentService.getAllDepartments()), HttpStatus.OK);
    }

}
