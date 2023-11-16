package com.vicheak.coreapp.api.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BaseSalaryLogRepository extends JpaRepository<BaseSalaryLog, Long>, JpaSpecificationExecutor<BaseSalaryLog> {

    Optional<BaseSalaryLog> findBaseSalaryLogByUuid(String uuid);

}
