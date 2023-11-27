package com.vicheak.coreapp.api.salarygross;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SalaryGrossRepository extends JpaRepository<SalaryGross, Integer>, JpaSpecificationExecutor<SalaryGross> {

    Optional<SalaryGross> findByNameIgnoreCase(String name);

    Boolean existsByNameIgnoreCase(String name);

    List<SalaryGross> findByGrossTypeId(Integer id);

}
