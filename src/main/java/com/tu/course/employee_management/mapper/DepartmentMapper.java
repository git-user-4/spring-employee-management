package com.tu.course.employee_management.mapper;

import com.tu.course.employee_management.dto.department.DepartmentRequestDTO;
import com.tu.course.employee_management.dto.department.DepartmentResponseDTO;
import com.tu.course.employee_management.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {EmployeeMapper.class})
public interface DepartmentMapper {

    DepartmentResponseDTO toDepartmentResponseDTO(Department department);

    List<DepartmentResponseDTO> toDepartmentResponseDTOList(List<Department> departments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Department toDepartment(DepartmentRequestDTO departmentRequestDTO);

}
