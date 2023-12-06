package com.vicheak.coreapp.api.slip;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaryPaymentRepository extends JpaRepository<SalaryPayment, Long> {

    Optional<SalaryPayment> findByUuid(String uuid);

}
