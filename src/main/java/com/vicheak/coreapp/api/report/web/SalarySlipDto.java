package com.vicheak.coreapp.api.report.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SalarySlipDto {

    @JsonIgnore
    private Long salaryPaymentId;

    private String salaryPaymentUuid;

    private LocalDateTime generatedSlipDateTime;

    private BigDecimal baseSalary;

    private BigDecimal salaryGrossBenefitAmount;

    private BigDecimal salaryGrossDeductionAmount;

    private BigDecimal finalSalary;

    private Integer month;

    private Integer year;

    private LocalDateTime paymentDateTime;

    private String paymentStatus;

}
