package com.vicheak.coreapp.api.report.web;

import com.vicheak.coreapp.api.salarygross.web.SalaryGrossDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReportSalaryPaymentDto {

    private String uuid;

    private LocalDateTime generatedSlipDateTime;

    private BigDecimal baseSalary;

    private BigDecimal salaryGrossBenefitAmount;

    private BigDecimal salaryGrossDeductionAmount;

    private BigDecimal finalSalary;

    private Integer month;

    private Integer year;

    private LocalDateTime paymentDateTime;

    private String paymentStatus;

    private List<SalaryGrossDto> salaryGrossDtos;

}
