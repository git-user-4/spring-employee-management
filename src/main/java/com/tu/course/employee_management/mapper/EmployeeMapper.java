package com.tu.course.employee_management.mapper;

import com.tu.course.employee_management.dto.auth.RegisterRequestDTO;
import com.tu.course.employee_management.dto.auth.RegisterResponseDTO;
import com.tu.course.employee_management.dto.employee.*;
import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.repository.projection.EmployeeNameProjection;
import com.tu.course.employee_management.service.CloudinaryService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class EmployeeMapper {

    @Autowired
    private  CloudinaryService cloudinaryService;

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "avatarImage",
            source = "avatarPublicId",
            qualifiedByName = "avatarUrl")
    public abstract EmployeeResponseDTO toEmployeeResponseDTO(Employee employee);

    public abstract List<EmployeeResponseDTO> toEmployeeResponseDTOList(List<Employee> employees);

    @Named("avatarUrl")
    protected String generateAvatarUrl(String avatarPublicId) {
        if (avatarPublicId == null || avatarPublicId.isBlank()) return null;
        return cloudinaryService.generateUrl(avatarPublicId, 200, 200); // Can adjust image size
    }

    public EmployeePageResponseDTO toEmployeePageResponseDTO(Page<Employee> page) {
        return new EmployeePageResponseDTO(
                page.getContent().stream().map(this::toEmployeeResponseDTO).toList(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Mapping(target = "jwtToken", source = "jwtToken")
    @Mapping(target = "employeeData", source = "employee")
    public abstract RegisterResponseDTO toRegisterResponseDTO(Employee employee, String jwtToken);

    public abstract EmployeeSummaryResponseDTO toEmployeeSummaryResponseDTO(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    public abstract Employee toEmployee(RegisterRequestDTO registerRequestDTO);

    public abstract List<EmployeeNameResponseDTO> toEmployeeNameResponseDTOList(List<EmployeeNameProjection> employeeNameProjections);

    public abstract List<EmployeeNameResponseDTO> toEmployeeNameResponseDTOListFromProjectionDTOList(List<EmployeeNameProjectionDTO> employeeNameProjectionDTOs);

    @Mapping(target = "department", ignore = true)
    public abstract void updateEmployeeFromRequestDTO(@MappingTarget Employee entity, RegisterRequestDTO dto);

    // Showcasing alternative method to trim endpoint inputs
    /*
    @AfterMapping
    protected void trimEmployeeFields(@MappingTarget Employee entity, EmployeeRequestDTO dto) {
        entity.setFirstName(dto.firstName().trim());
        entity.setLastName(dto.lastName().trim());
        if (entity.getPhoneNumber() != null) entity.setPhoneNumber(dto.phoneNumber().trim());
    }
    */

}
