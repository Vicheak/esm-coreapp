package com.vicheak.coreapp.api.slip;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaryPaymentGrossRepository extends JpaRepository<SalaryPaymentGross, Long> {

    void deleteBySalaryPayment(SalaryPayment salaryPayment);

    List<SalaryPaymentGross> findBySalaryPaymentId(Long salaryPaymentId);

}
