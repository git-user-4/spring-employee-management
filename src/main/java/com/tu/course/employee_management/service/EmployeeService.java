package com.tu.course.employee_management.service;

import com.tu.course.employee_management.dto.employee.EmployeeNameProjectionDTO;
import com.tu.course.employee_management.dto.employee.EmployeeRequestDTO;
import com.tu.course.employee_management.exception.ResourceNotFoundException;
import com.tu.course.employee_management.mapper.EmployeeMapper;
import com.tu.course.employee_management.model.Department;
import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.repository.EmployeeRepository;
import com.tu.course.employee_management.repository.projection.EmployeeNameProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final EmployeeMapper employeeMapper;

    @Transactional
    public Employee saveEmployee(EmployeeRequestDTO employeeRequestDTO) {
        String requestDepartmentName = employeeRequestDTO.departmentName();
        Employee employee = employeeMapper.toEmployee(employeeRequestDTO);

        if (requestDepartmentName != null) {
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
        if (employeeRepository.existsById(id))
            employeeRepository.deleteById(id);
        else
            throw new ResourceNotFoundException(Employee.class, id);

    }

    @Transactional
    public Employee patchEmployeeFirstName(Long id, String newFirstName) {
        Optional<Employee> fetchedEmployee = employeeRepository.findById(id);

        if (fetchedEmployee.isPresent()) {
            Employee newEmployee = fetchedEmployee.get();
            newEmployee.setFirstName(newFirstName.trim());
            return employeeRepository.save(newEmployee);
        } else throw new ResourceNotFoundException(Employee.class, id);

    }

    @Transactional
    public Employee putEmployee(Long id, EmployeeRequestDTO employeeRequestDTO) {
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

}
