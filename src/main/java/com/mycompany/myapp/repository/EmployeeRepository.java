package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Employee;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@SuppressWarnings("unused")
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    @Query("select distinct employee from Employee employee left join fetch employee.managerIds")
    List<Employee> findAllWithEagerRelationships();

    @Query("select employee from Employee employee left join fetch employee.managerIds where employee.id =:id")
    Employee findOneWithEagerRelationships(@Param("id") Long id);

}
