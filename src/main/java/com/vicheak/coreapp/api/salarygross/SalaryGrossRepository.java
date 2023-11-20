package com.vicheak.coreapp.api.salarygross;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaryGrossRepository extends JpaRepository<SalaryGross, Integer> {

    Optional<SalaryGross> findByNameIgnoreCase(String name);

    Boolean existsByNameIgnoreCase(String name);

}
