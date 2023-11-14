package com.vicheak.coreapp.api.department;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByNameIgnoreCase(String name);

    Boolean existsByNameIgnoreCaseOrPhone(String name, String phone);

    Boolean existsByNameIgnoreCase(String name);

    Boolean existsByPhone(String phone);

}
