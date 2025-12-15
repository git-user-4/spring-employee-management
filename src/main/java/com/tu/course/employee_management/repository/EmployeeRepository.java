package com.tu.course.employee_management.repository;

import com.tu.course.employee_management.dto.employee.EmployeeNameProjectionDTO;
import com.tu.course.employee_management.model.Employee;
import com.tu.course.employee_management.model.Role;
import com.tu.course.employee_management.repository.projection.EmployeeNameProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Modifying
    @Query("UPDATE Employee e SET e.department = null WHERE e.id = :empId")
    int removeDepartment(@Param("empId") Long employeeId);

    List<Employee> findByPhoneNumberStartingWith(String startingCombination);

    @Query("""
            SELECT e
            FROM Employee e
            WHERE e.lastName = :searchedLastName
              AND EXISTS (
                  SELECT 1
                  FROM Employee e2
                  WHERE e2.department = e.department
                    AND e2.lastName = e.lastName
                    AND e2.id <> e.id
              )
            """)
    List<Employee> findEmployeesWithSameLastNameInSameDepartment(@Param("searchedLastName") String searchedLastName);

    // --- Projections ---
    List<EmployeeNameProjection> findByLastName(String lastName);

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT new com.tu.course.employee_management.dto.employee.EmployeeNameProjectionDTO(e.firstName, e.lastName) FROM Employee e WHERE e.firstName = :firstName")
    List<EmployeeNameProjectionDTO> findByFirstName(@Param("firstName") String firstName);
    // -------------------

}
