package com.tu.course.employee_management.service;

import com.tu.course.employee_management.config.security.CustomUserPrincipal;
import com.tu.course.employee_management.dto.employee.EmployeeNameProjectionDTO;
import com.tu.course.employee_management.dto.auth.RegisterRequestDTO;
import com.tu.course.employee_management.exception.DuplicateResourceException;
import com.tu.course.employee_management.exception.ResourceNotFoundException;
import com.tu.course.employee_management.mapper.EmployeeMapper;
import com.tu.course.employee_management.model.Department;
import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.model.Role;
import com.tu.course.employee_management.repository.EmployeeRepository;
import com.tu.course.employee_management.repository.projection.EmployeeNameProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final EmployeeMapper employeeMapper;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Employee registerEmployee(RegisterRequestDTO registerRequestDTO) {

        // Prevent duplicate accounts for same email (before DB throws for unique column constraint)
        if (employeeRepository.existsByEmail(registerRequestDTO.email()))
            throw new DuplicateResourceException(Employee.class, "email", registerRequestDTO.email());

        Employee employee = employeeMapper.toEmployee(registerRequestDTO);

        employee.setPassword(passwordEncoder.encode(registerRequestDTO.password()));

        employee.setRole(Role.USER);

        String requestDepartmentName = registerRequestDTO.departmentName();
        if (requestDepartmentName != null && !requestDepartmentName.isBlank()) {
            Department department = departmentService.getDepartmentByNameOrCreate(requestDepartmentName);
            employee.setDepartment(department);
        }

        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) employeeRepository.deleteById(id);
        else throw new ResourceNotFoundException(Employee.class, id);

    }

    @Transactional
    public Employee patchEmployeeFirstName(Long id, String newFirstName) {
        Employee fetchedEmployee = getEmployeeOrThrow(id);
        fetchedEmployee.setFirstName(newFirstName);
        return employeeRepository.save(fetchedEmployee);
    }

    @Transactional
    public Employee putEmployee(Long id, RegisterRequestDTO employeeRequestDTO) {
        Employee fetchedEmployee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Employee.class, id));

        employeeMapper.updateEmployeeFromRequestDTO(fetchedEmployee, employeeRequestDTO);

        if (employeeRequestDTO.departmentName() != null) {
            Department department = departmentService.getDepartmentByNameOrCreate(employeeRequestDTO.departmentName());
            fetchedEmployee.setDepartment(department);
        }
        return employeeRepository.save(fetchedEmployee);
    }

    @Transactional
    public Employee assignEmployeeDepartment(Long employeeId, Long departmentId) {
        Employee employee = getEmployeeOrThrow(employeeId);
        Department department = departmentService.getDepartmentOrThrow(departmentId);

        employee.setDepartment(department);
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeOrThrow(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Employee.class, id));
    }

    @Transactional
    public void deleteEmployeeDepartmentViaQuery(Long employeeId) {
        int rowsAffected = employeeRepository.removeDepartment(employeeId);
        if (rowsAffected == 0) throw new ResourceNotFoundException(Employee.class, employeeId);
    }

    public List<Employee> getEmployeesWithPhoneNumberStartingWith(String combination) {
        return employeeRepository.findByPhoneNumberStartingWith(combination);
    }

    public List<Employee> getEmployeesWithSameLastNameInSameDepartment(String name) {
        return employeeRepository.findEmployeesWithSameLastNameInSameDepartment(name);
    }

    public List<EmployeeNameProjection> getEmployeeNamesByLastName(String lastName) {
        return employeeRepository.findByLastName(lastName);
    }

    public List<EmployeeNameProjectionDTO> getEmployeeNamesByFirstName(String firstName) {
        return employeeRepository.findByFirstName(firstName);
    }

    @Transactional
    public Employee patchEmployeeAvatar(Long employeeId, MultipartFile avatarImage) {
        Employee fetchedEmployee = getEmployeeOrThrow(employeeId);
        String imagePublicId = cloudinaryService.uploadImage(avatarImage);
        fetchedEmployee.setAvatarPublicId(imagePublicId);
        return employeeRepository.save(fetchedEmployee);
    }

    @Transactional
    public void deleteEmployeeAvatar(Long employeeId) {
        Employee fetchedEmployee = getEmployeeOrThrow(employeeId);
        cloudinaryService.deleteImage(fetchedEmployee.getAvatarPublicId());
        fetchedEmployee.setAvatarPublicId(null);
        employeeRepository.save(fetchedEmployee);
    }

//    @Transactional
//    public Employee patchEmployeeRole(Long employeeId, Role newRole) {
//        Employee fetchedEmployee = getEmployeeOrThrow(employeeId);
//        Role currentRole = fetchedEmployee.getRole();
//
//        fetchedEmployee.setRole(newRole);
//        return employeeRepository.save(fetchedEmployee);
//    }

    @Transactional
    public Employee patchEmployeeRole(Long employeeId, Role newRole) {
        Employee fetchedEmployee = getEmployeeOrThrow(employeeId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal principal = (CustomUserPrincipal) auth.getPrincipal();
        Long currentUserId = principal.getEmployeeId();

        if (fetchedEmployee.getId().equals(currentUserId)) {
            throw new IllegalStateException("You cannot change your own role");
        }

        fetchedEmployee.setRole(newRole);
        return employeeRepository.save(fetchedEmployee);
    }

}
