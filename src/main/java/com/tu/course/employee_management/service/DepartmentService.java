package com.tu.course.employee_management.service;

import com.tu.course.employee_management.dto.department.DepartmentRequestDTO;
import com.tu.course.employee_management.exception.ResourceNotFoundException;
import com.tu.course.employee_management.mapper.DepartmentMapper;
import com.tu.course.employee_management.model.Department;
import com.tu.course.employee_management.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    public Department saveDepartment(DepartmentRequestDTO departmentRequestDTO) {
        Department department = departmentMapper.toDepartment(departmentRequestDTO);
        department.setName(department.getName());
        return departmentRepository.save(department);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        if (departmentRepository.existsById(id)) departmentRepository.deleteById(id);
        else throw new ResourceNotFoundException(Department.class, id);
    }

    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }

    public Department getDepartmentOrThrow(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Department.class, id));
    }

    public Department getDepartmentByNameOrCreate(String name) {
        Optional<Department> fetchedDepartment = departmentRepository.findByName(name);
        return fetchedDepartment.orElseGet(() -> departmentRepository.save(new Department(name)));
    }

}
