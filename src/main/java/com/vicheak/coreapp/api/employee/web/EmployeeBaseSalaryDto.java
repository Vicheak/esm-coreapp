package com.vicheak.coreapp.api.employee.web;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record EmployeeBaseSalaryDto(@NotNull(message = "Base Salary cannot be null!")
                                    @Positive(message = "Base Salary must be positive!")
                                    @DecimalMin(value = "0.000001")
                                    BigDecimal baseSalary) {
}
