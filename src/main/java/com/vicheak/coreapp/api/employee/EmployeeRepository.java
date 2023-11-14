package com.vicheak.coreapp.api.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Boolean existsByEmailIgnoreCase(String email);

    Boolean existsByPhone(String phone);

    Optional<Employee> findByUuid(String uuid);

    List<Employee> findByDepartmentName(String name);

}
